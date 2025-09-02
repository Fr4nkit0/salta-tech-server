package com.saltaTech.category.application.mapper;

import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.category.domain.dto.request.CategoryCreateRequest;
import com.saltaTech.category.domain.dto.request.CategoryUpdateRequest;
import com.saltaTech.category.domain.dto.response.CategoryResponse;
import com.saltaTech.category.domain.persistence.Category;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import com.saltaTech.organization.domain.persistence.Organization;
import com.saltaTech.organization.domain.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {
	public Category toCategory(CategoryCreateRequest createRequest, Organization organization){
		if(createRequest == null) return null;
		return Category.builder()
				.organization(organization)
				.name(createRequest.name())
				.build();
	}

	public CategoryResponse toCategoryResponse(Category category){
		if(category == null) return null;
		return new CategoryResponse(
				category.getId(),
				category.getName()
		);
	}

	public void toUpdateCategory(Category oldCategory, CategoryUpdateRequest updateRequest){
		if(updateRequest.name() != null){
			oldCategory.setName(updateRequest.name());
		}
	}
}
