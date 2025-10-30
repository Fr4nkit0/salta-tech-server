package com.saltaTech.sale.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saltaTech.customer.domain.dto.response.CustomerResponse;
import com.saltaTech.payment.domain.dto.response.PaymentResponse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SalesDetailsResponse(
        Long id,
        @JsonProperty("customer_info")
        CustomerResponse customerInfo,
        String status,
        BigDecimal total,
        @JsonProperty("total_paid")
        BigDecimal totalPaid,
        BigDecimal balance,
        @JsonProperty("main_payment_method")
        String mainPaymentMethod,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate date,
		List<ItemsResponse> items,
        List<PaymentResponse> payments
) implements Serializable {
}
