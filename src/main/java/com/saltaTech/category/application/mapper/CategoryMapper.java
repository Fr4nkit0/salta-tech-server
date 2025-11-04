package com.saltaTech.category.application.mapper;

import com.saltaTech.category.domain.dto.request.CategoryCreateRequest;
import com.saltaTech.category.domain.dto.request.CategoryUpdateRequest;
import com.saltaTech.category.domain.dto.response.CategoryResponse;
import com.saltaTech.category.domain.persistence.Category;
import com.saltaTech.branch.domain.persistence.Branch;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {
	public Category toCategory(CategoryCreateRequest createRequest, Branch branch){
		if(createRequest == null) return null;
		return Category.builder()
				.branch(branch)
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
