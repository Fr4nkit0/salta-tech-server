package com.saltaTech.customer.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saltaTech.common.domain.util.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

public record CustomerCreateRequest(
		@JsonProperty("first_name")
		@Size(min = 2 , max =  50, message = "{generic.size}")
		@NotBlank(message = "{generic.notBlank}")
		String firstName,
		@JsonProperty("last_name")
		@Size(min = 2 , max =  50, message = "{generic.size}")
		@NotBlank(message = "{generic.notBlank}")
		String lastName,
		@NotBlank(message = "{generic.notBlank}")
		@Email
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
