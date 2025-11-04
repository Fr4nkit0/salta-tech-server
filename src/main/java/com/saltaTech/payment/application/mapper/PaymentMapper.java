package com.saltaTech.payment.application.mapper;

import com.saltaTech.branch.domain.persistence.Branch;
import com.saltaTech.payment.domain.dto.request.PaymentCreateRequest;
import com.saltaTech.payment.domain.dto.request.PaymentUpdateRequest;
import com.saltaTech.payment.domain.dto.response.PaymentResponse;
import com.saltaTech.payment.domain.persistence.Payment;
import com.saltaTech.payment.domain.persistence.PaymentMethod;
import com.saltaTech.payment.domain.persistence.Transaction;
import com.saltaTech.payment.domain.util.Type;
import com.saltaTech.sale.domain.persistence.Sale;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class PaymentMapper {
	public Payment toPayment (PaymentCreateRequest createRequest, Branch branch, PaymentMethod paymentMethod, Sale sale){
		if (createRequest == null) return  null;
		final var transaction = Transaction.builder()
				.branch(branch)
				.type(Type.IN)
				.amount(createRequest.amount())
				.build();
		return  Payment.builder()
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
