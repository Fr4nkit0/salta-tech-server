package com.saltaTech.customer.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saltaTech.common.domain.util.Gender;
import com.saltaTech.customer.domain.util.Status;

import java.io.Serializable;
import java.time.LocalDate;

public record CustomerResponse(
		Long id,
		String name,
		String email,
		@JsonProperty("phone_number")
		String phoneNumber,
		String dni,
		String cuil,
		Gender gender,
		@JsonProperty("birth_date")
		LocalDate birthDate,
		Status status
) implements Serializable {
}
