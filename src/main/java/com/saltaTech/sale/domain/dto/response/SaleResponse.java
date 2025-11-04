package com.saltaTech.sale.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleResponse (
        Long id,
        String branch,
        @JsonProperty("customer_name")
        String customerName,
        String status,
        BigDecimal total,
        @JsonProperty("total_paid")
        BigDecimal totalPaid,
        BigDecimal balance,
        @JsonProperty("main_payment_method")
        String mainPaymentMethod,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate date
) implements Serializable {
}