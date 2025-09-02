package com.saltaTech.organization.domain.specification;

import com.saltaTech.organization.domain.dto.request.OrganizationSearchCriteria;
import com.saltaTech.organization.domain.persistence.Organization;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

public class OrganizationSpecification implements Specification<Organization> {
	private final OrganizationSearchCriteria searchCriteria;

	public OrganizationSpecification(OrganizationSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		var predicates = new ArrayList<Predicate>() ;
		if (StringUtils.hasText(searchCriteria.name())){
			final var nameLike = criteriaBuilder.like(
							criteriaBuilder.lower(root.get("name")),
							"%" + searchCriteria.name().toLowerCase() + "%"
			);
			predicates.add(nameLike);
		}
		if (StringUtils.hasText(searchCriteria.slug())){
			final var slugLike = criteriaBuilder.like(
					criteriaBuilder.lower(root.get("slug")),
					"%" + searchCriteria.slug().toLowerCase() + "%"
			);
			predicates.add(slugLike);
		}
		final var enabledPredicate = criteriaBuilder.isTrue(root.get("enabled"));
		predicates.add(enabledPredicate);
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}
}
