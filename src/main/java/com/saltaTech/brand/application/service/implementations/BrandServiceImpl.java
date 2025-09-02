package com.saltaTech.brand.application.service.implementations;

import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.brand.application.exceptions.BrandNotFoundException;
import com.saltaTech.brand.application.exceptions.NoBrandsFoundException;
import com.saltaTech.brand.application.mapper.BrandMapper;
import com.saltaTech.brand.application.service.interfaces.BrandService;
import com.saltaTech.brand.domain.dto.request.BrandCreateRequest;
import com.saltaTech.brand.domain.dto.request.BrandSearchCriteria;
import com.saltaTech.brand.domain.dto.request.BrandUpdateRequest;
import com.saltaTech.brand.domain.dto.response.BrandResponse;
import com.saltaTech.brand.domain.persistence.Brand;
import com.saltaTech.brand.domain.repository.BrandRepository;
import com.saltaTech.brand.domain.specification.BrandSpecification;
import com.saltaTech.common.application.aop.OrganizationSecured;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import com.saltaTech.organization.domain.repository.OrganizationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@OrganizationSecured
@Transactional
public class BrandServiceImpl implements BrandService {
	private final BrandMapper brandMapper;
	private final BrandRepository brandRepository;
	private final OrganizationRepository organizationRepository;


	public BrandServiceImpl(BrandMapper brandMapper, BrandRepository brandRepository, OrganizationRepository organizationRepository) {
		this.brandMapper = brandMapper;
		this.brandRepository = brandRepository;
		this.organizationRepository = organizationRepository;
	}

	@Override
	public Page<BrandResponse> findAll(Pageable pageable, BrandSearchCriteria searchCriteria) {
		final var brandSpecification = new BrandSpecification(searchCriteria);
		var brands = brandRepository.findAll(brandSpecification, pageable);
		if(brands.isEmpty()){
			throw new NoBrandsFoundException(searchCriteria);
		}
		return brands.map(brandMapper::toBrandResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public BrandResponse findById(Long id) {
		return brandMapper.toBrandResponse(findByIdEntity(id));
	}

	@Override
	public BrandResponse create(BrandCreateRequest createRequest) {
		final var slug = OrganizationContext.getOrganizationSlug() ;
		final var organization = organizationRepository
				.findActiveBySlug(slug)
				.orElseThrow(()-> new OrganizationNotFoundException(slug)) ;
		return brandMapper.toBrandResponse(
				brandRepository.save(brandMapper.toBrand(createRequest, organization))
		);
	}

	@Override
	public BrandResponse update(BrandUpdateRequest updateRequest, Long id) {
		var oldBrand = findByIdEntity(id);
		brandMapper.toUpdateBrand(oldBrand, updateRequest);
		var updatedBrand = brandRepository.save(oldBrand);
		return brandMapper.toBrandResponse(updatedBrand);
	}

	@Override
	public void deleteById(Long id) {
		brandRepository.deleteById(id);
	}

	private Brand findByIdEntity(Long id){
		return brandRepository.findById(id).orElseThrow(()-> new BrandNotFoundException(id));
	}
}
