package com.saltaTech.brand.application.service.implementations;

import com.saltaTech.auth.application.security.authentication.context.BranchContext;
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
import com.saltaTech.common.application.aop.BranchSecured;
import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.branch.domain.repository.BranchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@BranchSecured
@Transactional
public class BrandServiceImpl implements BrandService {
	private final BrandMapper brandMapper;
	private final BrandRepository brandRepository;
	private final BranchRepository branchRepository;


	public BrandServiceImpl(BrandMapper brandMapper, BrandRepository brandRepository, BranchRepository branchRepository) {
		this.brandMapper = brandMapper;
		this.brandRepository = brandRepository;
		this.branchRepository = branchRepository;
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
		final var tenant = BranchContext.getBranchTenant() ;
		final var branch = branchRepository
				.findActiveByTenant(tenant)
				.orElseThrow(()-> new BranchNotFoundException(tenant)) ;
		return brandMapper.toBrandResponse(
				brandRepository.save(brandMapper.toBrand(createRequest, branch))
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
