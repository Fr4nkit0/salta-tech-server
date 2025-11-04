package com.saltaTech.branch.application.controller;

import com.saltaTech.auth.application.security.authentication.context.BranchContext;
import com.saltaTech.branch.application.service.interfaces.BranchService;
import com.saltaTech.branch.domain.dto.request.BranchCreateRequest;
import com.saltaTech.branch.domain.dto.request.BranchSearchCriteria;
import com.saltaTech.branch.domain.dto.response.BranchResponse;
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
@RequestMapping("/branches")
public class BranchController {
	private final BranchService branchService;

	public BranchController(BranchService branchService) {
		this.branchService = branchService;
	}

	@GetMapping
	public ResponseEntity<Page<BranchResponse>> findAll (Pageable pageable,
														 @RequestParam(required = false)String name,
														 @RequestParam(required = false)String identifier){
		final var searchCriteria = new BranchSearchCriteria(name,identifier);
		return ResponseEntity.ok(branchService.findAll(pageable,searchCriteria));
	}

	@GetMapping("/{branchId}")
	public ResponseEntity<BranchResponse> findById(@PathVariable Long branchId){
		return ResponseEntity.ok(branchService.findById(branchId));
	}
	@GetMapping("/tenant")
	public ResponseEntity<BranchResponse> findById(){
		return ResponseEntity.ok(branchService
				.findOrganizationByIdentifier(BranchContext.getBranchTenant()));
	}
	@PostMapping
	public ResponseEntity<BranchResponse> create (@RequestBody BranchCreateRequest organizationCreateRequest,
												  HttpServletRequest request){
		final var savedOrganization = branchService.create(organizationCreateRequest);
		final var baseURL = request.getRequestURL().toString();
		final var newLocation = URI.create(baseURL+"/"+savedOrganization.id());
		return ResponseEntity.created(newLocation).body(savedOrganization);
	}
}
