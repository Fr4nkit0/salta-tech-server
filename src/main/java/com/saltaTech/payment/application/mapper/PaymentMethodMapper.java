package com.saltaTech.payment.application.mapper;
import com.saltaTech.organization.domain.persistence.Organization;
import com.saltaTech.payment.domain.dto.request.PaymentMethodUpdateRequest;
import com.saltaTech.payment.domain.dto.request.PaymentMethodCreateRequest;
import com.saltaTech.payment.domain.dto.response.PaymentMethodResponse;
import com.saltaTech.payment.domain.persistence.PaymentMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PaymentMethodMapper {
	public PaymentMethod toPaymentMethod (PaymentMethodCreateRequest createRequest,Organization organization)  {

		return  PaymentMethod.builder()
				.organization(organization)
				.type(createRequest.type())
				.name(createRequest.name())
				.build();
	}
	public PaymentMethodResponse toPaymentMethodResponse (PaymentMethod paymentMethod){
		return new PaymentMethodResponse(
				paymentMethod.getId(),
				paymentMethod.getName(),
				paymentMethod.getType().name()
		);
	}
	public  void toUpdatePaymentMethod (PaymentMethod oldPaymentType, PaymentMethodUpdateRequest updateRequest){
		if (StringUtils.hasText(updateRequest.name())){
			oldPaymentType.setName(updateRequest.name());
		}
	}
}
