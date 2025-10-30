package com.saltaTech.organization.application.service.implementations;
import com.saltaTech.organization.application.exceptions.NoOrganizationsFoundException;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import com.saltaTech.organization.application.mapper.OrganizationMapper;
import com.saltaTech.organization.application.service.interfaces.OrganizationService;
import com.saltaTech.organization.domain.dto.request.OrganizationCreateRequest;
import com.saltaTech.organization.domain.dto.request.OrganizationSearchCriteria;
import com.saltaTech.organization.domain.dto.request.OrganizationUpdateRequest;
import com.saltaTech.organization.domain.dto.response.OrganizationResponse;
import com.saltaTech.organization.domain.persistence.Organization;
import com.saltaTech.organization.domain.repository.OrganizationRepository;
import com.saltaTech.organization.domain.specification.OrganizationSpecification;
import com.saltaTech.product.application.exceptions.NoProductsFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {
	private final OrganizationRepository organizationRepository;
	private final OrganizationMapper organizationMapper;

	public OrganizationServiceImpl(OrganizationMapper organizationMapper, OrganizationRepository organizationRepository) {
		this.organizationMapper = organizationMapper;
		this.organizationRepository = organizationRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<OrganizationResponse> findAll(Pageable pageable, OrganizationSearchCriteria searchCriteria) {
		final var organizationSpecification = new OrganizationSpecification(searchCriteria);
		var organizations = organizationRepository.findAll(organizationSpecification, pageable);
		if (organizations.isEmpty()) {
			throw new NoOrganizationsFoundException(searchCriteria) ;
		}
		return organizations.map(organizationMapper::toOrganizationResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public OrganizationResponse findById(Long id) {
		return organizationMapper.toOrganizationResponse(findByIdEntity(id));
	}

	@Transactional(readOnly = true)
	@Override
	public OrganizationResponse findOrganizationBySlug(String slug) {
	    return organizationMapper.toOrganizationResponse(
				organizationRepository.findActiveByTenant(slug)
						.orElseThrow(()-> new OrganizationNotFoundException(slug))
		);
	}

	@Override
	public OrganizationResponse create(OrganizationCreateRequest createRequest) {
		var savedOrganization = organizationRepository.save(organizationMapper.toOrganization(createRequest));
		return organizationMapper.toOrganizationResponse(savedOrganization);
	}

	@Override
	public OrganizationResponse update(OrganizationUpdateRequest updateRequest, Long id) {
		return null;
	}

	@Override
	public void deleteOrganization(String slug) {
		// Implementar el borrado logico
	}

	@Override
	public void deleteById(Long id) {
		organizationRepository.deleteById(id);
	}

	private Organization findByIdEntity (Long id){
		return organizationRepository.findById(id)
				.orElseThrow(()->new OrganizationNotFoundException(id));
	}
}
