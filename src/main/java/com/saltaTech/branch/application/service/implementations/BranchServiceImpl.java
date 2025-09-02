package com.saltaTech.branch.application.service.implementations;

import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.branch.application.exceptions.NoBranchesException;
import com.saltaTech.branch.application.mapper.BranchMapper;
import com.saltaTech.branch.application.service.interfaces.BranchService;
import com.saltaTech.branch.domain.dto.request.BranchCreateRequest;
import com.saltaTech.branch.domain.dto.request.BranchSearchCriteria;
import com.saltaTech.branch.domain.dto.request.BranchUpdateRequest;
import com.saltaTech.branch.domain.dto.response.BranchResponse;
import com.saltaTech.branch.domain.persistence.Branch;
import com.saltaTech.branch.domain.repository.BranchRepository;
import com.saltaTech.branch.domain.specification.BranchSpecification;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import com.saltaTech.organization.domain.repository.OrganizationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BranchServiceImpl implements BranchService {
	private final BranchRepository branchRepository;
	private final OrganizationRepository organizationRepository;
	private final BranchMapper branchMapper;

	public BranchServiceImpl(BranchMapper branchMapper,
							 BranchRepository branchRepository,
							 OrganizationRepository organizationRepository) {
		this.branchMapper = branchMapper;
		this.branchRepository = branchRepository;
		this.organizationRepository = organizationRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<BranchResponse> findAll(Pageable pageable, BranchSearchCriteria searchCriteria) {
		final var branchSpecification = new BranchSpecification(searchCriteria);
		var branches = branchRepository.findAll(branchSpecification, pageable);
		if (branches.isEmpty()) {
			throw new NoBranchesException(searchCriteria);
		}
		return branches.map(branchMapper::toBranchResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public BranchResponse findById(Long id) {
		return branchMapper.toBranchResponse(findByIdEntity(id));
	}

	@Override
	public BranchResponse create(BranchCreateRequest createRequest) {
		final var slug = OrganizationContext.getOrganizationSlug();
		final var organization = organizationRepository.findActiveBySlug(slug)
				.orElseThrow(()-> new OrganizationNotFoundException(slug));
		return branchMapper.toBranchResponse(
				branchRepository.save(branchMapper.toBranch(createRequest,organization))
		);
	}

	@Override
	public BranchResponse update(BranchUpdateRequest updateRequest, Long id) {
		return null;
	}

	@Override
	public void deleteById(Long id) {
		branchRepository.deleteById(id);
	}

	private Branch findByIdEntity (Long id){
		return branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
	}
}
