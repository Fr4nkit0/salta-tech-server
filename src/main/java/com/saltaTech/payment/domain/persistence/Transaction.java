package com.saltaTech.payment.domain.persistence;

import com.saltaTech.common.domain.persistence.Filters;
import com.saltaTech.common.domain.persistence.OrganizationFilterDefinition;
import com.saltaTech.payment.domain.util.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class)
@Filter(
		name = Filters.ORGANIZATION_FILTER,
		condition = "EXISTS (SELECT 1 FROM organizations o WHERE "+
				"o.id = organization_id AND o.slug = :" + Filters.ORGANIZATION_SLUG_PARAM + ")"
)
public class Transaction extends OrganizationFilterDefinition {
	@Enumerated(EnumType.STRING)
	@Column(nullable = false,length = 3)
	private Type type;
	@Column(nullable = false,precision = 15,scale = 2)
	private BigDecimal amount;
}
