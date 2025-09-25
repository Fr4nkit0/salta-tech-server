package com.saltaTech.category.application.services.implementations;
import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.category.application.exceptions.CategoryNotFoundException;
import com.saltaTech.category.application.exceptions.NoCategoriesFoundException;
import com.saltaTech.category.application.mapper.CategoryMapper;
import com.saltaTech.category.application.services.interfaces.CategoryService;
import com.saltaTech.category.domain.dto.request.CategoryCreateRequest;
import com.saltaTech.category.domain.dto.request.CategorySearchCriteria;
import com.saltaTech.category.domain.dto.request.CategoryUpdateRequest;
import com.saltaTech.category.domain.dto.response.CategoryResponse;
import com.saltaTech.category.domain.persistence.Category;
import com.saltaTech.category.domain.repository.CategoryRepository;
import com.saltaTech.category.domain.specification.CategorySpecification;
import com.saltaTech.common.application.aop.OrganizationSecured;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import com.saltaTech.organization.domain.repository.OrganizationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@OrganizationSecured
public class CategoryServiceImpl implements CategoryService {
	private final CategoryMapper categoryMapper;
	private final CategoryRepository categoryRepository;
	private final OrganizationRepository organizationRepository;


	public CategoryServiceImpl(CategoryMapper categoryMapper, CategoryRepository categoryRepository, OrganizationRepository organizationRepository) {
		this.categoryMapper = categoryMapper;
		this.categoryRepository = categoryRepository;
		this.organizationRepository = organizationRepository;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CategoryResponse> findAll(Pageable pageable, CategorySearchCriteria searchCriteria) {
		final var categorySpecification = new CategorySpecification(searchCriteria);
		var categories = categoryRepository.findAll(categorySpecification, pageable);
		if(categories.isEmpty()){
			throw new NoCategoriesFoundException(searchCriteria);
		}
		return categories.map(categoryMapper::toCategoryResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public CategoryResponse findById(Long id) {return categoryMapper.toCategoryResponse(findByIdEntity(id));
	}

	@Override
	public CategoryResponse create(CategoryCreateRequest createRequest) {
		final var tenant = OrganizationContext.getOrganizationTenant() ;
		final var organization = organizationRepository
				.findActiveByTenant(tenant)
				.orElseThrow(()-> new OrganizationNotFoundException(tenant)) ;
		return categoryMapper.toCategoryResponse(
				categoryRepository.save(categoryMapper.toCategory(createRequest,organization))
		);
	}

	@Override
	public CategoryResponse update(CategoryUpdateRequest updateRequest, Long id) {
		var oldCategory = findByIdEntity(id);
		categoryMapper.toUpdateCategory(oldCategory, updateRequest);
		var updateCategory = categoryRepository.save(oldCategory);
		return categoryMapper.toCategoryResponse(updateCategory);
	}

	@Override
	public void deleteById(Long id) {
		categoryRepository.deleteById(id);
	}

	private Category findByIdEntity(Long id){
		return categoryRepository.findById(id).orElseThrow(()-> new CategoryNotFoundException(id));
	}
}
