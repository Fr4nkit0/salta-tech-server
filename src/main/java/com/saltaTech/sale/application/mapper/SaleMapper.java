package com.saltaTech.sale.application.mapper;

import com.saltaTech.customer.application.mapper.CustomerMapper;
import com.saltaTech.customer.domain.persistence.Customer;
import com.saltaTech.organization.domain.persistence.Organization;
import com.saltaTech.payment.application.mapper.PaymentMapper;
import com.saltaTech.payment.domain.dto.response.PaymentResponse;
import com.saltaTech.payment.domain.persistence.Payment;
import com.saltaTech.product.domain.persistence.Product;
import com.saltaTech.sale.domain.dto.request.SaleCreateRequest;
import com.saltaTech.sale.domain.dto.response.SalesDetailsResponse;
import com.saltaTech.sale.domain.dto.response.SaleResponse;
import com.saltaTech.sale.domain.persistence.Sale;
import com.saltaTech.sale.domain.persistence.SaleDetails;
import com.saltaTech.sale.domain.util.SaleStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SaleMapper {
	private final CustomerMapper customerMapper;
	private final PaymentMapper paymentMapper;

	public SaleMapper(CustomerMapper customerMapper, PaymentMapper paymentMapper) {
		this.customerMapper = customerMapper;
		this.paymentMapper = paymentMapper;
	}

	public Sale toSale (SaleCreateRequest createRequest, Organization organization, Customer customer) {
		if (createRequest==null) return null;
		return Sale.builder()
				.organization(organization)
				.customer(customer)
				.status(SaleStatus.PENDIENTE)
				.total(createRequest.total())
				.build();
	}
	public SaleDetails toSaleDetail (Organization organization, Sale sale, Product product, Integer quantity, BigDecimal price){
		if (sale==null) return null;
		return SaleDetails.builder()
				.organization(organization)
				.sale(sale)
				.product(product)
				.quantity(quantity)
				.price(price)
				.build();
	}

	public SaleResponse toSaleResponse(Sale sale) {
		if (sale == null) return null;

		BigDecimal totalPaid = calculateTotalPaid(sale);
		String mainPaymentMethod = findMainPaymentMethod(sale);
		BigDecimal balance = sale.getTotal().subtract(totalPaid);

		return new SaleResponse(
				sale.getId(),
				sale.getOrganization().getName(),
				sale.getCustomer().getPerson().getFullName(),
				sale.getStatus().toString(),
				sale.getTotal(),
				totalPaid,
				balance,
				mainPaymentMethod,
				sale.getCreatedDate().toLocalDate()
		);
	}

	public SalesDetailsResponse toSaleDetailResponse(Sale sale) {
		if (sale == null) return null;

		BigDecimal totalPaid = calculateTotalPaid(sale);
		String mainPaymentMethod = findMainPaymentMethod(sale);
		BigDecimal balance = sale.getTotal().subtract(totalPaid);

		List<PaymentResponse> paymentResponses = sale.getPayments().stream()
				.map(paymentMapper::toPaymentResponse)
				.toList();

		return new SalesDetailsResponse(
				sale.getId(),
				sale.getOrganization().getName(),
				customerMapper.toCustomerResponse(sale.getCustomer()),
				sale.getStatus().toString(),
				sale.getTotal(),
				totalPaid,
				balance,
				mainPaymentMethod,
				sale.getCreatedDate().toLocalDate(),
				paymentResponses
		);
	}

	private BigDecimal calculateTotalPaid(Sale sale) {
		return sale.getPayments().stream()
				.map(Payment::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private String findMainPaymentMethod(Sale sale) {
		Map<String, BigDecimal> amountByPaymentType = sale.getPayments().stream()
				.collect(Collectors.groupingBy(
						payment -> payment.getPaymentMethod().getType().name(), // o getCode(), depende tu entidad
						Collectors.reducing(BigDecimal.ZERO,
								Payment::getAmount,
								BigDecimal::add
						)
				));

		// Busca el paymentType con el mayor monto
		return amountByPaymentType.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.orElse(null);
	}

}
