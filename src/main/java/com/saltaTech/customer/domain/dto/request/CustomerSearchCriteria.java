package com.saltaTech.customer.domain.dto.request;

public record CustomerSearchCriteria(
		String firstName,
		String lastName,
		String name,
		String email,
		String phoneNumber,
		String dni,
		String cuil
) {
}
