package com.saltaTech.category.application.services.implementations;
import com.saltaTech.auth.application.security.authentication.context.BranchContext;
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
import com.saltaTech.common.application.aop.BranchSecured;
import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.branch.domain.repository.BranchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@BranchSecured
public class CategoryServiceImpl implements CategoryService {
	private final CategoryMapper categoryMapper;
	private final CategoryRepository categoryRepository;
	private final BranchRepository branchRepository;


	public CategoryServiceImpl(CategoryMapper categoryMapper, CategoryRepository categoryRepository, BranchRepository branchRepository) {
		this.categoryMapper = categoryMapper;
		this.categoryRepository = categoryRepository;
		this.branchRepository = branchRepository;
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
		final var tenant = BranchContext.getBranchTenant() ;
		final var organization = branchRepository
				.findActiveByTenant(tenant)
				.orElseThrow(()-> new BranchNotFoundException(tenant)) ;
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
