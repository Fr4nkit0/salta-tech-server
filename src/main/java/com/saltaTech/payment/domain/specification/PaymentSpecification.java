package com.saltaTech.payment.domain.specification;

import com.saltaTech.payment.domain.dto.request.PaymentSearchCriteria;
import com.saltaTech.payment.domain.persistence.Payment;
import com.saltaTech.payment.domain.persistence.PaymentMethod;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;


public class PaymentSpecification implements Specification<Payment> {
	private final PaymentSearchCriteria searchCriteria;

	public PaymentSpecification(PaymentSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		var predicates=new ArrayList<Predicate>();
		Join<Payment, PaymentMethod> paymentMethodJoin = root.join("paymentMethod");
		if ((searchCriteria.amount() != null) && (searchCriteria.amount().compareTo(BigDecimal.ZERO) > 0)){
			final var amountEqual = criteriaBuilder.equal(
					root.get("amount"),
					searchCriteria.amount());
			predicates.add(amountEqual);
		}
		if (StringUtils.hasText(searchCriteria.paymentType())){
			final var paymentType = criteriaBuilder.like(
					paymentMethodJoin.get("type").as(String.class),
					"%".concat(searchCriteria.paymentType()).concat("%"));
			predicates.add(paymentType);
		}
		final var enabledPredicate = criteriaBuilder.isTrue(root.get("enabled"));
		predicates.add(enabledPredicate);
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}
}
