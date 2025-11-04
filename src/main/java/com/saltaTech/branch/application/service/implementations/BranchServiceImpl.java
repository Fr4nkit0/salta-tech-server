package com.saltaTech.branch.application.service.implementations;
import com.saltaTech.branch.application.exceptions.NoBranchesFoundException;
import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.branch.application.mapper.BranchMapper;
import com.saltaTech.branch.application.service.interfaces.BranchService;
import com.saltaTech.branch.domain.dto.request.BranchCreateRequest;
import com.saltaTech.branch.domain.dto.request.BranchSearchCriteria;
import com.saltaTech.branch.domain.dto.request.BranchUpdateRequest;
import com.saltaTech.branch.domain.dto.response.BranchResponse;
import com.saltaTech.branch.domain.persistence.Branch;
import com.saltaTech.branch.domain.repository.BranchRepository;
import com.saltaTech.branch.domain.specification.BranchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BranchServiceImpl implements BranchService {
	private final BranchRepository branchRepository;
	private final BranchMapper branchMapper;

	public BranchServiceImpl(BranchMapper branchMapper, BranchRepository branchRepository) {
		this.branchMapper = branchMapper;
		this.branchRepository = branchRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<BranchResponse> findAll(Pageable pageable, BranchSearchCriteria searchCriteria) {
		final var branchSpecification = new BranchSpecification(searchCriteria);
		var organizations = branchRepository.findAll(branchSpecification, pageable);
		if (organizations.isEmpty()) {
			throw new NoBranchesFoundException(searchCriteria) ;
		}
		return organizations.map(branchMapper::toBranchResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public BranchResponse findById(Long id) {
		return branchMapper.toBranchResponse(findByIdEntity(id));
	}

	@Transactional(readOnly = true)
	@Override
	public BranchResponse findOrganizationByIdentifier(String identifier) {
	    return branchMapper.toBranchResponse(
				branchRepository.findActiveByTenant(identifier)
						.orElseThrow(()-> new BranchNotFoundException(identifier))
		);
	}

	@Override
	public BranchResponse create(BranchCreateRequest createRequest) {
		var savedOrganization = branchRepository.save(branchMapper.toBranch(createRequest));
		return branchMapper.toBranchResponse(savedOrganization);
	}

	@Override
	public BranchResponse update(BranchUpdateRequest updateRequest, Long id) {
		return null;
	}

	@Override
	public void deleteOrganizationByIdentifier(String identifier) {
		// Implementar el borrado logico
	}

	@Override
	public void deleteById(Long id) {
		branchRepository.deleteById(id);
	}

	private Branch findByIdEntity (Long id){
		return branchRepository.findById(id)
				.orElseThrow(()->new BranchNotFoundException(id));
	}
}
