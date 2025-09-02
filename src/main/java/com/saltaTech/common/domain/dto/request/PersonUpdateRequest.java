package com.saltaTech.common.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saltaTech.common.domain.util.Gender;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

public record PersonUpdateRequest(
		@JsonProperty("first_name")
		@Size(min = 2 , max =  50, message = "{generic.size}")
		String firstName,
		@JsonProperty("last_name")
		@Size(min = 2 , max =  50, message = "{generic.size}")
		String lastName,
		String email,
		@JsonProperty("phone_number")
		String phoneNumber,
		String dni,
		String cuil,
		Gender gender,
		@JsonProperty("birth_date")
		LocalDate birthDate
) implements Serializable {
}
