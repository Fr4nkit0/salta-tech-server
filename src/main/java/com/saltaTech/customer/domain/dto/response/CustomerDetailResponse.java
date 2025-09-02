package com.saltaTech.customer.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saltaTech.common.domain.util.Gender;
import com.saltaTech.customer.domain.util.Status;

import java.io.Serializable;
import java.time.LocalDate;

public record CustomerDetailResponse(
        Long id,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
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
