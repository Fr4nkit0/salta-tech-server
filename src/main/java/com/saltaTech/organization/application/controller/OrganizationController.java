package com.saltaTech.organization.application.controller;

import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.organization.application.service.interfaces.OrganizationService;
import com.saltaTech.organization.domain.dto.request.OrganizationCreateRequest;
import com.saltaTech.organization.domain.dto.request.OrganizationSearchCriteria;
import com.saltaTech.organization.domain.dto.response.OrganizationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {
	private final OrganizationService organizationService;

	public OrganizationController(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@GetMapping
	public ResponseEntity<Page<OrganizationResponse>> findAll (Pageable pageable,
															   @RequestParam(required = false)String name,
															   @RequestParam(required = false)String slug){
		final var searchCriteria = new OrganizationSearchCriteria(name,slug);
		return ResponseEntity.ok(organizationService.findAll(pageable,searchCriteria));
	}

	@GetMapping("/{organizationId}")
	public ResponseEntity<OrganizationResponse> findById(@PathVariable Long organizationId){
		return ResponseEntity.ok(organizationService.findById(organizationId));
	}
	@GetMapping("/slug")
	public ResponseEntity<OrganizationResponse> findById(){
		return ResponseEntity.ok(organizationService
				.findOrganizationBySlug(OrganizationContext.getOrganizationSlug()));
	}
	@PostMapping
	public ResponseEntity<OrganizationResponse> create (@RequestBody OrganizationCreateRequest organizationCreateRequest,
														HttpServletRequest request){
		final var savedOrganization = organizationService.create(organizationCreateRequest);
		final var baseURL = request.getRequestURL().toString();
		final var newLocation = URI.create(baseURL+"/"+savedOrganization.id());
		return ResponseEntity.created(newLocation).body(savedOrganization);
	}
}
