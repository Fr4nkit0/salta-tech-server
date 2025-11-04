package com.saltaTech.branch.domain.specification;

import com.saltaTech.branch.domain.dto.request.BranchSearchCriteria;
import com.saltaTech.branch.domain.persistence.Branch;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

public class BranchSpecification implements Specification<Branch> {
	private final BranchSearchCriteria searchCriteria;

	public BranchSpecification(BranchSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<Branch> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		var predicates = new ArrayList<Predicate>() ;
		if (StringUtils.hasText(searchCriteria.name())){
			final var nameLike = criteriaBuilder.like(
							criteriaBuilder.lower(root.get("name")),
							"%" + searchCriteria.name().toLowerCase() + "%"
			);
			predicates.add(nameLike);
		}
		if (StringUtils.hasText(searchCriteria.identifier())){
			final var slugLike = criteriaBuilder.like(
					criteriaBuilder.lower(root.get("indentifier")),
					"%" + searchCriteria.identifier().toLowerCase() + "%"
			);
			predicates.add(slugLike);
		}
		final var enabledPredicate = criteriaBuilder.isTrue(root.get("enabled"));
		predicates.add(enabledPredicate);
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}
}
