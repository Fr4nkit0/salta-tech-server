package com.saltaTech.brand.application.controller;

import com.saltaTech.brand.application.service.interfaces.BrandService;
import com.saltaTech.brand.domain.dto.request.BrandCreateRequest;
import com.saltaTech.brand.domain.dto.request.BrandSearchCriteria;
import com.saltaTech.brand.domain.dto.request.BrandUpdateRequest;
import com.saltaTech.brand.domain.dto.response.BrandResponse;
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

@RequestMapping("brands")
@RestController
public class BrandController {
	private final BrandService brandService;

	public BrandController(BrandService brandService) {
		this.brandService = brandService;
	}
	@GetMapping
	public ResponseEntity<Page<BrandResponse>> findAll(Pageable pageable,
													   @RequestParam(required = false) String name) {
		final var searchCriteria = new BrandSearchCriteria(name);
		return ResponseEntity.ok(brandService.findAll(pageable, searchCriteria));
	}

	@GetMapping("/{brandId}")
	public ResponseEntity<BrandResponse> findById(@PathVariable Long brandId) {
		return ResponseEntity.ok(brandService.findById(brandId));
	}

	@PostMapping
	public ResponseEntity<BrandResponse> create(@Valid @RequestBody BrandCreateRequest brandCreateRequest,
												HttpServletRequest request) {
		final var savedBrand = brandService.create(brandCreateRequest);
		var baseURL = request.getRequestURL().toString();
		var newURI = URI.create(baseURL + "/" + savedBrand.id());
		return ResponseEntity.created(newURI).body(savedBrand);
	}

	@PutMapping("/{brandId}")
	public ResponseEntity<BrandResponse> update(@PathVariable Long brandId,
												@Valid @RequestBody BrandUpdateRequest brandUpdateRequest) {
		return ResponseEntity.ok(brandService.update(brandUpdateRequest, brandId));
	}

	@DeleteMapping("/{brandId}")
	public ResponseEntity<Void> delete(@PathVariable Long brandId) {
		brandService.deleteById(brandId);
		return ResponseEntity.noContent().build();
	}
}
