package com.saltaTech.category.application.controller;

import com.saltaTech.brand.domain.dto.request.BrandCreateRequest;
import com.saltaTech.brand.domain.dto.response.BrandResponse;
import com.saltaTech.category.application.services.interfaces.CategoryService;
import com.saltaTech.category.domain.dto.request.CategoryCreateRequest;
import com.saltaTech.category.domain.dto.request.CategorySearchCriteria;
import com.saltaTech.category.domain.dto.request.CategoryUpdateRequest;
import com.saltaTech.category.domain.dto.response.CategoryResponse;
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

@RestController
@RequestMapping("categories")
public class CategoryController {
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public ResponseEntity<Page<CategoryResponse>> findAll(Pageable pageable,
														  @RequestParam(required = false) String name) {
		final var searchCriteria = new CategorySearchCriteria(name);
		return ResponseEntity.ok(categoryService.findAll(pageable, searchCriteria));
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryResponse> findById(@PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.findById(categoryId));
	}

	@PostMapping
	public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest,
												HttpServletRequest request) {
		final var savedCategory = categoryService.create(categoryCreateRequest);
		var baseURL = request.getRequestURL().toString();
		var newURI = URI.create(baseURL + "/" + savedCategory.id());
		return ResponseEntity.created(newURI).body(savedCategory);
	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryResponse> update(@PathVariable Long categoryId,
												@Valid @RequestBody CategoryUpdateRequest brandUpdateRequest) {
		return ResponseEntity.ok(categoryService.update(brandUpdateRequest, categoryId));
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> delete(@PathVariable Long categoryId) {
		categoryService.deleteById(categoryId);
		return ResponseEntity.noContent().build();
	}
}
