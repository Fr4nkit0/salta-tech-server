package com.saltaTech.customer.application.mapper;

import com.saltaTech.common.application.mapper.PersonMapper;
import com.saltaTech.customer.domain.dto.request.CustomerCreateRequest;
import com.saltaTech.customer.domain.dto.request.CustomerUpdateRequest;
import com.saltaTech.customer.domain.dto.response.CustomerDetailResponse;
import com.saltaTech.customer.domain.dto.response.CustomerResponse;
import com.saltaTech.customer.domain.persistence.Customer;
import com.saltaTech.customer.domain.util.Status;
import com.saltaTech.branch.domain.persistence.Branch;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.function.Consumer;

@Service
public class CustomerMapper {
	private final PersonMapper personMapper;

	public CustomerMapper(PersonMapper personMapper) {
		this.personMapper = personMapper;
	}

	public Customer toCustomer (CustomerCreateRequest customerCreateRequest, Branch branch){
		if (customerCreateRequest== null) return  null;
		return Customer.builder()
				.person(personMapper.toPerson(customerCreateRequest))
				.branch(branch)
				.status(Status.ACTIVO)
				.build();
	}
	public CustomerResponse toCustomerResponse (Customer customer){
		if (customer==null) return null;
		return new CustomerResponse(
				customer.getId(),
				customer.getPerson().getFullName(),
				customer.getPerson().getEmail(),
				customer.getPerson().getPhoneNumber(),
				customer.getPerson().getDni(),
				customer.getPerson().getCuil(),
				customer.getPerson().getGender(),
				customer.getPerson().getBirthDate(),
				customer.getStatus()
		);
	}

	public CustomerDetailResponse toCustomerDetailResponse (Customer customer){
		if (customer==null) return null;
		return new CustomerDetailResponse(
				customer.getId(),
				customer.getPerson().getFirstName(),
				customer.getPerson().getLastName(),
				customer.getPerson().getEmail(),
				customer.getPerson().getPhoneNumber(),
				customer.getPerson().getDni(),
				customer.getPerson().getCuil(),
				customer.getPerson().getGender(),
				customer.getPerson().getBirthDate(),
				customer.getStatus());
	}
	public void toUpdateCustomer(Customer oldCustomer, CustomerUpdateRequest request) {
		if (request == null) return;
		var person = oldCustomer.getPerson();
		updateIfHasText(request.firstName(), person::setFirstName);
		updateIfHasText(request.lastName(), person::setLastName);
		updateIfHasText(request.email(), person::setEmail);
		updateIfHasText(request.phoneNumber(), person::setPhoneNumber);
		updateIfHasText(request.dni(), person::setDni);
		updateIfHasText(request.cuil(), person::setCuil);
		oldCustomer.setStatus(request.status());
	}

	private void updateIfHasText(String value, Consumer<String> setter) {
		if (StringUtils.hasText(value)) {
			setter.accept(value);
		}
	}


}
