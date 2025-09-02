package com.saltaTech.payment.domain.specification;

import com.saltaTech.payment.domain.dto.request.PaymentMethodSearchCriteria;
import com.saltaTech.payment.domain.persistence.PaymentMethod;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

public class PaymenMethodSpecification implements Specification<PaymentMethod> {
	private final PaymentMethodSearchCriteria searchCriteria;

	public PaymenMethodSpecification(PaymentMethodSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<PaymentMethod> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
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
