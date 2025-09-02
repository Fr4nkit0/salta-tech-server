package com.saltaTech.payment.application.controller;

import com.saltaTech.payment.application.service.interfaces.PaymentMethodService;
import com.saltaTech.payment.domain.dto.request.PaymentMethodCreateRequest;
import com.saltaTech.payment.domain.dto.request.PaymentMethodSearchCriteria;
import com.saltaTech.payment.domain.dto.request.PaymentMethodUpdateRequest;
import com.saltaTech.payment.domain.dto.response.PaymentMethodResponse;
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

import java.net.URI;

@RequestMapping("payments-methods")
@RestController
public class PaymentMethodController {
	private final PaymentMethodService paymentMethodService;

	public PaymentMethodController(PaymentMethodService paymentMethodService) {
		this.paymentMethodService = paymentMethodService;
	}

	@GetMapping
	public ResponseEntity<Page<PaymentMethodResponse>> findAll(Pageable pageable,
															   @RequestParam(required = false) String name) {
		final var searchCriteria = new PaymentMethodSearchCriteria(name);
		return ResponseEntity.ok(paymentMethodService.findAll(pageable, searchCriteria));
	}

	@GetMapping("/{paymentMethodId}")
	public ResponseEntity<PaymentMethodResponse> findById(@PathVariable Long paymentMethodId) {
		return ResponseEntity.ok(paymentMethodService.findById(paymentMethodId));
	}

	@PostMapping
	public ResponseEntity<PaymentMethodResponse> create(@Valid @RequestBody PaymentMethodCreateRequest paymentMethodCreateRequest,
														HttpServletRequest request) {
		PaymentMethodResponse created = paymentMethodService.create(paymentMethodCreateRequest);
		String baseUrl = request.getRequestURL().toString();
		URI newLocation = URI.create(baseUrl + "/" + created.id());
		return ResponseEntity.created(newLocation).body(created);
	}

	@PutMapping("/{paymentMethodId}")
	public ResponseEntity<PaymentMethodResponse> update(@PathVariable Long paymentMethodId,
														@Valid @RequestBody PaymentMethodUpdateRequest paymentMethodUpdateRequest) {
		return ResponseEntity.ok(paymentMethodService.update(paymentMethodUpdateRequest, paymentMethodId));
	}

	@DeleteMapping("/{paymentMethodId}")
	public ResponseEntity<Void> delete(@PathVariable Long paymentMethodId) {
		paymentMethodService.deleteById(paymentMethodId);
		return ResponseEntity.noContent().build();
	}
}
