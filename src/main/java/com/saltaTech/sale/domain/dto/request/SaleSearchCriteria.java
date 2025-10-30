package com.saltaTech.sale.domain.dto.request;

import com.saltaTech.customer.domain.util.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleSearchCriteria(
		String customerName,       // nombre o apellido del cliente (LIKE)
		String dni,                // documento del cliente
		String email,              // correo electrónico del cliente
		String phoneNumber,        // teléfono del cliente
		Status status,             // estado de la venta (SaleStatus)
		BigDecimal minTotal,       // total mínimo
		BigDecimal maxTotal,       // total máximo
		LocalDate fromDate,        // fecha mínima de creación o emisión
		LocalDate toDate,          // fecha máxima de creación o emisión
		Boolean hasBalance         // ventas con saldo pendiente
) {
}
