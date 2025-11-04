package com.saltaTech.payment.application.service.implementations;

import com.saltaTech.auth.application.security.authentication.context.BranchContext;
import com.saltaTech.common.application.aop.BranchSecured;
import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.branch.domain.repository.BranchRepository;
import com.saltaTech.payment.application.exceptions.NoPaymentsFoundException;
import com.saltaTech.payment.application.exceptions.PaymentMethodFoundException;
import com.saltaTech.payment.application.mapper.PaymentMethodMapper;
import com.saltaTech.payment.application.service.interfaces.PaymentMethodService;
import com.saltaTech.payment.domain.dto.request.PaymentMethodUpdateRequest;
import com.saltaTech.payment.domain.dto.request.PaymentMethodCreateRequest;
import com.saltaTech.payment.domain.dto.request.PaymentMethodSearchCriteria;
import com.saltaTech.payment.domain.dto.response.PaymentMethodResponse;
import com.saltaTech.payment.domain.persistence.PaymentMethod;
import com.saltaTech.payment.domain.repository.PaymentMethodRepository;
import com.saltaTech.payment.domain.specification.PaymenMethodSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@BranchSecured
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {
	private final PaymentMethodMapper paymentMethodMapper;
	private final PaymentMethodRepository paymentMethodRepository;
	private final BranchRepository organizationRepository;

	public PaymentMethodServiceImpl(BranchRepository organizationRepository, PaymentMethodMapper paymentMethodMapper, PaymentMethodRepository paymentMethodRepository) {
		this.organizationRepository = organizationRepository;
		this.paymentMethodMapper = paymentMethodMapper;
		this.paymentMethodRepository = paymentMethodRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<PaymentMethodResponse> findAll(Pageable pageable, PaymentMethodSearchCriteria searchCriteria) {
		final var specification = new PaymenMethodSpecification(searchCriteria);
		var paymentTypes = paymentMethodRepository.findAll(specification,pageable);
		if (paymentTypes.isEmpty()) throw  new NoPaymentsFoundException(searchCriteria);
		return paymentTypes.map(paymentMethodMapper::toPaymentMethodResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public PaymentMethodResponse findById(Long id) {
		return paymentMethodMapper.toPaymentMethodResponse(
				findByEntity(id)
		);
	}

	@Override
	public PaymentMethodResponse create(PaymentMethodCreateRequest createRequest) {
		final var tenant = BranchContext.getBranchTenant() ;
		final var organization = organizationRepository
				.findActiveByTenant(tenant)
				.orElseThrow(()-> new BranchNotFoundException(tenant)) ;
		return paymentMethodMapper.toPaymentMethodResponse(
				paymentMethodRepository.save(
						paymentMethodMapper.toPaymentMethod(createRequest,organization)
				)
		);
	}

	@Override
	public PaymentMethodResponse update(PaymentMethodUpdateRequest updateRequest, Long id) {
		var oldPaymentMethod = findByEntity(id);
		paymentMethodMapper.toUpdatePaymentMethod(oldPaymentMethod,updateRequest);
		return paymentMethodMapper.toPaymentMethodResponse(
				paymentMethodRepository.save(oldPaymentMethod)
		);
	}

	@Override
	public void deleteById(Long id) {
		paymentMethodRepository.deleteById(id);

	}
	public PaymentMethod findByEntity (Long id){
		return paymentMethodRepository.findById(id)
				.orElseThrow(()-> new PaymentMethodFoundException(id));
	}
}
