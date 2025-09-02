package com.saltaTech.category.domain.specification;

import com.saltaTech.brand.domain.dto.request.BrandSearchCriteria;
import com.saltaTech.brand.domain.persistence.Brand;
import com.saltaTech.category.domain.dto.request.CategorySearchCriteria;
import com.saltaTech.category.domain.persistence.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

public class CategorySpecification implements Specification<Category> {
	private final CategorySearchCriteria searchCriteria;

	public CategorySpecification(CategorySearchCriteria searchCriteria){
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		var predicates=new ArrayList<Predicate>();
		if (StringUtils.hasText(searchCriteria.name())){
			final var nameLike = criteriaBuilder.like(
					root.get("name"),
					"%".concat(searchCriteria.name()).concat("%"));
			predicates.add(nameLike);
		}
		final var enabledPredicate = criteriaBuilder.isTrue(root.get("enabled"));
		predicates.add(enabledPredicate);
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}
}
