package com.saltaTech.payment.application.mapper;

import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.branch.domain.persistence.Branch;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import com.saltaTech.organization.domain.persistence.Organization;
import com.saltaTech.organization.domain.repository.OrganizationRepository;
import com.saltaTech.payment.application.exceptions.PaymentMethodFoundException;
import com.saltaTech.payment.domain.dto.request.Advance;
import com.saltaTech.payment.domain.dto.request.PaymentCreateRequest;
import com.saltaTech.payment.domain.dto.request.PaymentUpdateRequest;
import com.saltaTech.payment.domain.dto.response.PaymentResponse;
import com.saltaTech.payment.domain.persistence.Payment;
import com.saltaTech.payment.domain.persistence.PaymentMethod;
import com.saltaTech.payment.domain.persistence.Transaction;
import com.saltaTech.payment.domain.repository.PaymentMethodRepository;
import com.saltaTech.payment.domain.util.Type;
import com.saltaTech.sale.domain.persistence.Sale;
import com.saltaTech.sale.domain.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class PaymentMapper {
	public Payment toPayment (PaymentCreateRequest createRequest, Organization organization, Branch branch, PaymentMethod paymentMethod, Sale sale){
		if (createRequest == null) return  null;
		final var transaction = Transaction.builder()
				.organization(organization)
				.type(Type.IN)
				.amount(createRequest.amount())
				.build();
		return  Payment.builder()
				.organization(organization)
				.branch(branch)
				.transaction(transaction)
				.sale(sale)
				.paymentMethod(paymentMethod)
				.amount(createRequest.amount())
				.description(createRequest.description())
				.build();
	}

	public PaymentResponse toPaymentResponse (Payment payment){
		return new PaymentResponse(
				payment.getId(),
				payment.getAmount(),
				payment.getFormattedPaymentMethod(),
				payment.getDescription()
		);
	}
	public void toPaymentUpdate (Payment oldPayment, PaymentUpdateRequest updateRequest){
		if (StringUtils.hasText(updateRequest.description())){
			oldPayment.setDescription(updateRequest.description());
		}
	}

}
