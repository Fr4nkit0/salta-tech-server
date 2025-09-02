package com.saltaTech.payment.application.controller;

import com.saltaTech.payment.application.service.interfaces.PaymentService;
import com.saltaTech.payment.domain.dto.request.PaymentCreateRequest;
import com.saltaTech.payment.domain.dto.request.PaymentSearchCriteria;
import com.saltaTech.payment.domain.dto.request.PaymentUpdateRequest;
import com.saltaTech.payment.domain.dto.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;

@RequestMapping("payments")
@RestController
public class PaymentController {
	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}


	@GetMapping
	public ResponseEntity<Page<PaymentResponse>> findAll(Pageable pageable,
														 @RequestParam(required = false) BigDecimal amount,
														 @RequestParam(required = false, name = "payment_type") String paymentType) {
		final var searchCriteria = new PaymentSearchCriteria(amount, paymentType);
		return ResponseEntity.ok(paymentService.findAll(pageable, searchCriteria));
	}

	@GetMapping("/{paymentId}")
	public ResponseEntity<PaymentResponse> findById(@PathVariable Long paymentId) {
		return ResponseEntity.ok(paymentService.findById(paymentId));
	}

	@PostMapping
	public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentCreateRequest paymentCreateRequest,
												  HttpServletRequest request) {
		PaymentResponse created = paymentService.create(paymentCreateRequest);
		String baseUrl = request.getRequestURL().toString();
		URI newLocation = URI.create(baseUrl + "/" + created.id());
		return ResponseEntity.created(newLocation).body(created);
	}

	@PutMapping("/{paymentId}")
	public ResponseEntity<PaymentResponse> update(@PathVariable Long paymentId,
												  @Valid @RequestBody PaymentUpdateRequest paymentUpdateRequest) {
		return ResponseEntity.ok(paymentService.update(paymentUpdateRequest, paymentId));
	}

	@DeleteMapping("/{paymentId}")
	public ResponseEntity<Void> delete(@PathVariable Long paymentId) {
		paymentService.deleteById(paymentId);
		return ResponseEntity.noContent().build();
	}

}
