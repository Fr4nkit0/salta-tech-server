package com.saltaTech.product.domain.specification;

import com.saltaTech.brand.domain.persistence.Brand;
import com.saltaTech.category.domain.persistence.Category;
import com.saltaTech.product.domain.dto.request.ProductSearchCriteria;
import com.saltaTech.product.domain.persistence.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ProductSpecification implements Specification<Product> {
	private final ProductSearchCriteria searchCriteria;

	public ProductSpecification(ProductSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}


	@Override
	public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		var predicates = new ArrayList<Predicate>();
		Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);
		Join<Product, Brand> brandJoin = root.join("brand",JoinType.LEFT);
		if (StringUtils.hasText(searchCriteria.category())){
			final var categoryLike = criteriaBuilder.like(
					criteriaBuilder.lower(categoryJoin.get("name")),
					"%".concat(searchCriteria.category().toLowerCase()).concat("%"));
			predicates.add(categoryLike);
		}
		if (StringUtils.hasText(searchCriteria.brand())){
			final var brandLike = criteriaBuilder.like(
					criteriaBuilder.lower(brandJoin.get("name")),
					"%".concat(searchCriteria.category().toLowerCase()).concat("%"));
			predicates.add(brandLike);
		}

		if (StringUtils.hasText(searchCriteria.name())){
			final var nameLike = criteriaBuilder.like(
					criteriaBuilder.lower(root.get("name")),
					"%".concat(searchCriteria.name().toLowerCase()).concat("%"));
			predicates.add(nameLike);
		}
		if (searchCriteria.price()!=null &&searchCriteria.price().compareTo(BigDecimal.ZERO)>0){
			final var priceEqual = criteriaBuilder.equal(
					root.get("price"),searchCriteria.price());
			predicates.add(priceEqual);
		}
		final var enabledPredicate = criteriaBuilder.isTrue(root.get("enabled"));
		predicates.add(enabledPredicate);
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}
}
