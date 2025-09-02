package com.saltaTech.customer.domain.specification;

import com.saltaTech.common.domain.persistence.Person;
import com.saltaTech.customer.domain.dto.request.CustomerSearchCriteria;
import com.saltaTech.customer.domain.persistence.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

public class CustomerSpecification implements Specification<Customer> {
	private final CustomerSearchCriteria customerSearchCriteria;

	public CustomerSpecification(CustomerSearchCriteria customerSearchCriteria) {
		this.customerSearchCriteria = customerSearchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		var predicates=new ArrayList<Predicate>();
		Join<Customer, Person> person = root.join("person", JoinType.INNER);

		if (StringUtils.hasText(customerSearchCriteria.firstName())) {
			final var firstNameLike = criteriaBuilder.like(
					criteriaBuilder.lower(person.get("firstName")),
					"%" + customerSearchCriteria.firstName().toLowerCase() + "%"
			);
			predicates.add(firstNameLike);
		}

		if (StringUtils.hasText(customerSearchCriteria.lastName())) {
			final var lastNameLike = criteriaBuilder.like(
					criteriaBuilder.lower(person.get("lastName")),
					"%" + customerSearchCriteria.lastName().toLowerCase() + "%"
			);
			predicates.add(lastNameLike);
		}

		if (StringUtils.hasText(customerSearchCriteria.name())){
			Expression<String> name = criteriaBuilder.concat(
					criteriaBuilder.concat(
							person.get("firstName"),
							criteriaBuilder.literal(" ")
					),
					person.get("lastName")
			);
			final var nameLike = criteriaBuilder.like(
					criteriaBuilder.lower(name),
					"%" + customerSearchCriteria.name().toLowerCase() + "%"
			);
			predicates.add(nameLike);
		}

		if (StringUtils.hasText(customerSearchCriteria.email())) {
			final var emailLike = criteriaBuilder.like(
					criteriaBuilder.lower(person.get("email")),
					"%" + customerSearchCriteria.email().toLowerCase() + "%"
			);
			predicates.add(emailLike);
		}

		if (StringUtils.hasText(customerSearchCriteria.phoneNumber())) {
			final var phoneLike = criteriaBuilder.like(
					criteriaBuilder.lower(person.get("phoneNumber")),
					"%" + customerSearchCriteria.phoneNumber().toLowerCase() + "%"
			);
			predicates.add(phoneLike);
		}

		if (StringUtils.hasText(customerSearchCriteria.dni())) {
			final var dniLike = criteriaBuilder.like(
					criteriaBuilder.lower(person.get("dni")),
					"%" + customerSearchCriteria.dni().toLowerCase() + "%"
			);
			predicates.add(dniLike);
		}

		if (StringUtils.hasText(customerSearchCriteria.cuil())) {
			final var cuilLike = criteriaBuilder.like(
					criteriaBuilder.lower(person.get("cuil")),
					"%" + customerSearchCriteria.cuil().toLowerCase() + "%"
			);
			predicates.add(cuilLike);
		}
		final var enabledPredicate = criteriaBuilder.isTrue(root.get("enabled"));
		predicates.add(enabledPredicate);
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}
}
