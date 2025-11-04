package com.saltaTech.payment.domain.persistence;

import com.saltaTech.common.domain.persistence.Filters;
import com.saltaTech.common.domain.persistence.BranchFilterDefinition;
import com.saltaTech.payment.domain.util.PaymentType;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "payment_methods")
@EntityListeners(AuditingEntityListener.class)
@Filter(
		name = Filters.BRANCH_FILTER,
		condition = "EXISTS (SELECT 1 FROM branches b WHERE "+
				"b.id = branch_id AND b.identifier = :" + Filters.BRANCH_SLUG_PARAM + ")"
)
public class PaymentMethod extends BranchFilterDefinition {
	@Enumerated(EnumType.STRING)
	@Column(length = 30,nullable = false)
	private PaymentType type ;
	@Column(length = 30)
	private String name;
}
