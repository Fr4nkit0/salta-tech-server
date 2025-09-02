package com.saltaTech.common.application.mapper;

import com.saltaTech.common.domain.persistence.Person;
import com.saltaTech.customer.domain.dto.request.CustomerCreateRequest;
import org.springframework.stereotype.Service;

@Service
public class PersonMapper {

	public Person toPerson (CustomerCreateRequest createRequest){
		if (createRequest == null) return null;
		return Person.builder()
				.firstName(createRequest.firstName())
				.lastName(createRequest.lastName())
				.email(createRequest.email())
				.phoneNumber(createRequest.phoneNumber())
				.dni(createRequest.dni())
				.cuil(createRequest.cuil())
				.gender(createRequest.gender())
				.birthDate(createRequest.birthDate())
				.build();
	}
}
