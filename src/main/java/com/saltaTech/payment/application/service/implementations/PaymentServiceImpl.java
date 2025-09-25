package com.saltaTech.payment.application.service.implementations;

import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.common.application.aop.OrganizationSecured;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import com.saltaTech.organization.domain.repository.OrganizationRepository;
import com.saltaTech.payment.application.exceptions.NoPaymentsFoundException;
import com.saltaTech.payment.application.exceptions.PaymentMethodFoundException;
import com.saltaTech.payment.application.exceptions.PaymentNotFoundException;
import com.saltaTech.payment.application.mapper.PaymentMapper;
import com.saltaTech.payment.application.service.interfaces.PaymentService;
import com.saltaTech.payment.domain.dto.request.PaymentCreateRequest;
import com.saltaTech.payment.domain.dto.request.PaymentSearchCriteria;
import com.saltaTech.payment.domain.dto.request.PaymentUpdateRequest;
import com.saltaTech.payment.domain.dto.response.PaymentResponse;
import com.saltaTech.payment.domain.persistence.Payment;
import com.saltaTech.payment.domain.repository.PaymentMethodRepository;
import com.saltaTech.payment.domain.repository.PaymentRepository;
import com.saltaTech.payment.domain.specification.PaymentSpecification;
import com.saltaTech.sale.domain.repository.SaleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@OrganizationSecured
@Transactional
public class PaymentServiceImpl implements PaymentService {
	private final PaymentMapper paymentMapper;
	private final PaymentRepository paymentRepository;
	private final OrganizationRepository organizationRepository;
	private final PaymentMethodRepository paymentMethodRepository;
	private final SaleRepository saleRepository;


	public PaymentServiceImpl(PaymentMapper paymentMapper, PaymentRepository paymentRepository, OrganizationRepository organizationRepository, PaymentMethodRepository paymentMethodRepository, SaleRepository saleRepository) {
		this.paymentMapper = paymentMapper;
		this.paymentRepository = paymentRepository;
		this.organizationRepository = organizationRepository;
		this.paymentMethodRepository = paymentMethodRepository;
		this.saleRepository = saleRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<PaymentResponse> findAll(Pageable pageable, PaymentSearchCriteria searchCriteria) {
		final var specification = new PaymentSpecification(searchCriteria);
		var payments = paymentRepository.findAll(specification,pageable);
		if (payments.isEmpty()) throw  new NoPaymentsFoundException(searchCriteria);
		return payments.map(paymentMapper::toPaymentResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public PaymentResponse findById(Long id) {
		return paymentMapper.toPaymentResponse(findByIdEntity(id));
	}

	@Override
	public PaymentResponse create(PaymentCreateRequest createRequest) {
		final var tenant = OrganizationContext.getOrganizationTenant() ;
		final var organization = organizationRepository
				.findActiveByTenant(tenant)
				.orElseThrow(()-> new OrganizationNotFoundException(tenant)) ;
		final var paymentMethod = paymentMethodRepository.findById(createRequest.paymentMethodId())
				.orElseThrow(()-> new PaymentMethodFoundException(createRequest.paymentMethodId()));
		final var sale = saleRepository.findById(createRequest.saleId())
				.orElse(null);
		return paymentMapper.toPaymentResponse(
				paymentRepository.save(paymentMapper.toPayment(createRequest,organization,paymentMethod,sale))
		);
	}

	@Override
	public PaymentResponse update(PaymentUpdateRequest updateRequest, Long id) {
		var oldPayment = findByIdEntity(id);
		paymentMapper.toPaymentUpdate(oldPayment,updateRequest);
		return paymentMapper.toPaymentResponse(
				paymentRepository.save(oldPayment)
		);
	}

	@Override
	public void deleteById(Long id) {
		paymentRepository.deleteById(id);
	}

	private Payment findByIdEntity (Long id){
		return  paymentRepository.findById(id)
				.orElseThrow(()-> new PaymentNotFoundException(id));
	}
}
