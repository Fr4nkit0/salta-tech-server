package com.saltaTech.sale.application.service.implementations;

import com.saltaTech.auth.application.security.authentication.context.BranchContext;
import com.saltaTech.branch.domain.persistence.Branch;
import com.saltaTech.common.application.aop.BranchSecured;
import com.saltaTech.customer.domain.repository.CustomerRepository;
import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.branch.domain.repository.BranchRepository;
import com.saltaTech.payment.application.exceptions.PaymentMethodFoundException;
import com.saltaTech.payment.domain.persistence.Payment;
import com.saltaTech.payment.domain.persistence.Transaction;
import com.saltaTech.payment.domain.repository.PaymentMethodRepository;
import com.saltaTech.payment.domain.util.Type;
import com.saltaTech.product.domain.persistence.Product;
import com.saltaTech.product.domain.repository.ProductRepository;
import com.saltaTech.sale.application.exceptions.InsufficientStockException;
import com.saltaTech.sale.application.exceptions.InvalidSaleTotalException;
import com.saltaTech.sale.application.exceptions.NoSalesFoundException;
import com.saltaTech.sale.application.exceptions.SaleNotFoundException;
import com.saltaTech.sale.application.mapper.SaleMapper;
import com.saltaTech.sale.application.service.interfaces.SaleService;
import com.saltaTech.sale.domain.dto.request.SaleCreateRequest;
import com.saltaTech.sale.domain.dto.request.SalePaymentRequest;
import com.saltaTech.sale.domain.dto.request.SaleSearchCriteria;
import com.saltaTech.sale.domain.dto.request.SalesDetailsCreateRequest;
import com.saltaTech.sale.domain.dto.response.SalesDetailsResponse;
import com.saltaTech.sale.domain.dto.response.SaleResponse;
import com.saltaTech.sale.domain.persistence.Sale;
import com.saltaTech.sale.domain.persistence.SaleDetails;
import com.saltaTech.sale.domain.repository.SaleRepository;
import com.saltaTech.sale.domain.specification.SaleSpecification;
import com.saltaTech.sale.domain.util.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@BranchSecured
@Transactional
public class SaleServiceImpl implements SaleService {
	private final SaleMapper saleMapper;
	private final SaleRepository saleRepository;
	private final BranchRepository branchRepository;
	private final ProductRepository productRepository;
	private final CustomerRepository customerRepository;
	private final PaymentMethodRepository paymentMethodRepository;

	public SaleServiceImpl(CustomerRepository customerRepository, SaleMapper saleMapper, SaleRepository saleRepository, BranchRepository branchRepository, ProductRepository productRepository, PaymentMethodRepository paymentMethodRepository) {
		this.customerRepository = customerRepository;
		this.saleMapper = saleMapper;
		this.saleRepository = saleRepository;
		this.branchRepository = branchRepository;
		this.productRepository = productRepository;
		this.paymentMethodRepository = paymentMethodRepository;
	}

	@Override
	public SalesDetailsResponse create(SaleCreateRequest createRequest) {
		final var tenant = BranchContext.getBranchTenant();
		final var branch = branchRepository
				.findActiveByTenant(tenant)
				.orElseThrow(()-> new BranchNotFoundException(tenant));
		final var customer = customerRepository
				.findById(createRequest.customerId())
				.orElse(null);
		final var productsIds = createRequest.items().stream()
				.map(SalesDetailsCreateRequest::productId)
				.toList();
		var products = productRepository.findAllById(productsIds);
		if (products.size() != productsIds.size()) {
			throw new IllegalArgumentException("One or more products do not exist");
		}
		final var items = createRequest.items();
		var productsMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity())) ;
		validateTotal(productsMap, items,createRequest.total());
		validateStock(productsMap,items);
		reduceStock(productsMap, items);
		var sale = saleMapper.toSale(createRequest, branch, customer);
		sale.setSaleDetails(buildSalesDetails(createRequest.items(), productsMap, sale, branch));
		applyPayments(sale, createRequest, branch);
		return saleMapper.toSaleDetailResponse(saleRepository.save(sale));
	}

	@Transactional(readOnly = true)
    public Page<SaleResponse> findAll(Pageable pageable, SaleSearchCriteria searchCriteria) {
		final var salesSpecification = new SaleSpecification(searchCriteria);
        Page<Sale> sales = saleRepository.findAll(salesSpecification, pageable);
		if (sales.isEmpty()){
			throw new NoSalesFoundException(searchCriteria);
		}
        return sales.map(saleMapper::toSaleResponse);
    }


    @Transactional(readOnly = true)
    public SalesDetailsResponse findById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(id));
        return saleMapper.toSaleDetailResponse(sale);
    }
	private void validateTotal(Map<Long,Product> products, List<SalesDetailsCreateRequest> items,BigDecimal total) {
		BigDecimal totalAmountRequest = BigDecimal.ZERO ;
		BigDecimal expectedTotal = BigDecimal.ZERO;

		for (SalesDetailsCreateRequest item: items){
			Product product = products.get(item.productId());
			if(product==null){
				throw new IllegalArgumentException("Producto no encontrado: " + item.productId());
			}
			if (product.getPrice().compareTo(item.price())!= 0 ){
				throw new IllegalArgumentException("El precio del producto " + product.getName() +
						" no coincide con el precio registrado en el sistema.");
			}
			totalAmountRequest = totalAmountRequest.add(item.price().multiply(BigDecimal.valueOf(item.quantity())));
			expectedTotal = expectedTotal.add(product.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
		}

		if (expectedTotal.compareTo(total) != 0) {
			throw new InvalidSaleTotalException(
					"El total calculado en el sistema (" + expectedTotal +
							") no coincide con el total enviado en la venta (" + total + ")"
			);
		}

		if (expectedTotal.compareTo(totalAmountRequest) != 0) {
			throw new InvalidSaleTotalException(
					"El total calculado en el sistema (" + expectedTotal +
							") no coincide con el total enviado en la venta (" + totalAmountRequest + ")"
			);
		}
	}

	private void validateStock(Map<Long, Product> products, List<SalesDetailsCreateRequest> items) {
		for (var item : items) {
			var product = products.get(item.productId());
			if (product.getAvailableQuantity() < item.quantity()) {
				throw new InsufficientStockException("No hay suficiente stock para el producto: " + product.getName());
			}
		}
	}

	private void reduceStock(Map<Long, Product> products, List<SalesDetailsCreateRequest> items) {
		for (var item : items) {
			var product = products.get(item.productId());
			product.setAvailableQuantity(product.getAvailableQuantity() - item.quantity());
		}
		productRepository.saveAll(products.values());
	}
	private List<SaleDetails> buildSalesDetails(List<SalesDetailsCreateRequest> items, Map<Long, Product> productsMap, Sale sale, Branch branch) {
		return items.stream()
				.map(it -> saleMapper.toSaleDetail(
						branch,
						sale,
						productsMap.get(it.productId()),
						it.quantity(),
						it.price()
				))
				.toList();
	}

	private void applyPayments(Sale sale, SaleCreateRequest createRequest, Branch branch) {
		if (createRequest.payments() == null || createRequest.payments().isEmpty()) {
			return;
		}
		var totalAmount = createRequest.payments().stream()
				.map(SalePaymentRequest::amount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		if (totalAmount.compareTo(sale.getTotal()) > 0) {
			throw new IllegalArgumentException("El total de anticipos no puede superar el total de la venta");
		}
		if (totalAmount.compareTo(sale.getTotal()) == 0) {
			sale.setStatus(SaleStatus.COMPLETADO);
		}
		var transaction = Transaction.builder()
				.branch(branch)
				.type(Type.IN)
				.amount(totalAmount)
				.build();
		var payments = createRequest.payments().stream()
				.map(payment -> toPayment(branch, transaction, sale, payment))
				.toList();

		sale.setPayments(payments);
	}

	private Payment toPayment(Branch branch, Transaction transaction, Sale sale, SalePaymentRequest payment) {
		var paymentMethod = paymentMethodRepository.findById(payment.paymentMethodId())
				.orElseThrow(() -> new PaymentMethodFoundException(payment.paymentMethodId()));
		return Payment.builder()
				.branch(branch)
				.transaction(transaction)
				.sale(sale)
				.amount(payment.amount())
				.paymentMethod(paymentMethod)
				.description(payment.description())
				.build();
	}
}