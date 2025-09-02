package com.saltaTech.customer.domain.persistence;

import com.saltaTech.common.domain.persistence.Filters;
import com.saltaTech.common.domain.persistence.OrganizationFilterDefinition;
import com.saltaTech.common.domain.persistence.Person;
import com.saltaTech.customer.domain.util.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
@Filter(
		name = Filters.ORGANIZATION_FILTER,
		condition = "EXISTS (SELECT 1 FROM organizations o WHERE "+
				"o.id = organization_id AND o.slug = :" + Filters.ORGANIZATION_SLUG_PARAM + ")"
)
public class Customer extends OrganizationFilterDefinition {
	@Enumerated(EnumType.STRING)
	@Column(length = 100)
	private Status status;
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "person_id", nullable = false)
	private Person person;
}
