package com.saltaTech.customer.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saltaTech.customer.domain.util.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

public record CustomerUpdateRequest(
        // Campos de PersonUpdateRequest
        @JsonProperty("first_name")
        @Size(min = 2, max = 50, message = "{generic.size}")
        String firstName,
        @JsonProperty("last_name")
        @Size(min = 2, max = 50, message = "{generic.size}")
        String lastName,
        String email,
        @JsonProperty("phone_number")
        String phoneNumber,
        String dni,
        String cuil,
        com.saltaTech.common.domain.util.Gender gender,
        @JsonProperty("birth_date")
        LocalDate birthDate,
        @NotNull(message = "El status es obligatorio")
        Status status
) implements Serializable {
} 