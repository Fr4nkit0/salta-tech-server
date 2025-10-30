package com.saltaTech.sale.domain.specification;

import com.saltaTech.common.domain.persistence.Person;
import com.saltaTech.customer.domain.persistence.Customer;
import com.saltaTech.payment.domain.persistence.Payment;
import com.saltaTech.sale.domain.dto.request.SaleSearchCriteria;
import com.saltaTech.sale.domain.persistence.Sale;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SaleSpecification implements Specification<Sale> {
	private static final String FIELD_ID = "id";
	private static final String FIELD_TOTAL = "total";
	private static final String FIELD_STATUS = "status";
	private static final String FIELD_ENABLED = "enabled";
	private static final String FIELD_CREATED_DATE = "createdDate";
	private static final String FIELD_AMOUNT = "amount";
	private static final String FIELD_FIRST_NAME = "firstName";
	private static final String FIELD_LAST_NAME = "lastName";
	private static final String FIELD_DNI = "dni";
	private static final String FIELD_EMAIL = "email";
	private static final String FIELD_PHONE_NUMBER = "phoneNumber";
	private final SaleSearchCriteria searchCriteria;

	public SaleSpecification(SaleSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<Sale> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		var predicates = new ArrayList<Predicate>();
		Join<Sale, Customer> customerJoin = root.join("customer", JoinType.LEFT);
		Join<Customer, Person> personJoin = customerJoin.join("person", JoinType.LEFT);
		Join<Sale,Payment> paymentJoin = root.join("payments", JoinType.LEFT);
		if (StringUtils.hasText(searchCriteria.customerName())){
			Expression<String> name = criteriaBuilder.concat(
					criteriaBuilder.concat(
							personJoin.get(FIELD_FIRST_NAME),
							criteriaBuilder.literal(" ")
					),
					personJoin.get(FIELD_LAST_NAME)
			);
			final var nameLike = criteriaBuilder.like(
					criteriaBuilder.lower(name),
					"%" + searchCriteria.customerName().toLowerCase() + "%"
			);
			predicates.add(nameLike);
		}
		if (StringUtils.hasText(searchCriteria.dni())) {
			final var dniEqual = criteriaBuilder.equal(
					personJoin.get(FIELD_DNI),searchCriteria.dni()
			);
			predicates.add(dniEqual);
		}
		if (StringUtils.hasText(searchCriteria.email())) {
			final var emailLike = criteriaBuilder.like(
					criteriaBuilder.lower(personJoin.get(FIELD_EMAIL)),
					"%" + searchCriteria.email().toLowerCase() + "%"
			);
			predicates.add(emailLike);
		}
		if (StringUtils.hasText(searchCriteria.phoneNumber())) {
			final var phoneLike= criteriaBuilder.like(
					personJoin.get(FIELD_PHONE_NUMBER),
					"%" + searchCriteria.phoneNumber() + "%"
			);
			predicates.add(phoneLike);
		}
		if (searchCriteria.status() != null){
			final var statusEqual = criteriaBuilder.equal(
					root.get(FIELD_STATUS),searchCriteria.status()
			);
			predicates.add(statusEqual);
		}
		if(searchCriteria.minTotal()!=null&& searchCriteria.minTotal().compareTo(BigDecimal.ZERO)>0 ){
			final var minTotalEqual = criteriaBuilder.greaterThanOrEqualTo(
					root.get(FIELD_TOTAL),searchCriteria.minTotal()
			);
			predicates.add(minTotalEqual);
		}
		if(searchCriteria.maxTotal()!=null && searchCriteria.maxTotal().compareTo(BigDecimal.ZERO)>0){
			final var maxTotalEqual = criteriaBuilder.lessThanOrEqualTo(
					root.get(FIELD_TOTAL),searchCriteria.maxTotal()
			);
			predicates.add(maxTotalEqual);
		}
		if (searchCriteria.fromDate() != null) {
			final var fromDateEqual = criteriaBuilder.greaterThanOrEqualTo(
					root.get(FIELD_CREATED_DATE), searchCriteria.fromDate().atStartOfDay()
			);
			predicates.add(fromDateEqual);
		}
		if (searchCriteria.toDate() != null) {
			final var toDateEqual = criteriaBuilder.lessThanOrEqualTo(
					root.get(FIELD_CREATED_DATE), searchCriteria.toDate().atTime(23, 59, 59)
			);
			predicates.add(toDateEqual);
		}
		if (searchCriteria.hasBalance() != null) {
			final var totalPayments = criteriaBuilder.coalesce(
					criteriaBuilder.sum(paymentJoin.get(FIELD_AMOUNT)),
					BigDecimal.ZERO
			);
			query.groupBy(root.get(FIELD_ID));
			query.distinct(true);
			if (searchCriteria.hasBalance()) {
				final var hasPendingBalance = criteriaBuilder.greaterThan(
						criteriaBuilder.diff(root.get(FIELD_TOTAL), totalPayments),
						BigDecimal.ZERO
				);
				query.having(hasPendingBalance);
			} else {
				final var hasFullyPaid = criteriaBuilder.lessThanOrEqualTo(
						criteriaBuilder.diff(root.get(FIELD_TOTAL), totalPayments),
						BigDecimal.ZERO
				);
				query.having(hasFullyPaid);
			}

		}
		final var enabledPredicate = criteriaBuilder.isTrue(root.get(FIELD_ENABLED));
		predicates.add(enabledPredicate);
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}


}

