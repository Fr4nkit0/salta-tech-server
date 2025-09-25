package com.saltaTech.payment.domain.persistence;

import com.saltaTech.common.domain.persistence.Filters;
import com.saltaTech.common.domain.persistence.OrganizationFilterDefinition;
import com.saltaTech.sale.domain.persistence.Sale;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "payments")
@EntityListeners(AuditingEntityListener.class)
@Filter(
		name = Filters.ORGANIZATION_FILTER,
		condition = "EXISTS (SELECT 1 FROM organizations o WHERE "+
				"o.id = organization_id AND o.slug = :" + Filters.ORGANIZATION_SLUG_PARAM + ")"
)
public class Payment extends OrganizationFilterDefinition {
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "transaction_id", nullable = false)
	private Transaction transaction;
	@ManyToOne
	@JoinColumn(name = "sale_id")
	private Sale sale;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "payment_method_id", nullable = false)
	private PaymentMethod paymentMethod;
	@Column(nullable = false,precision = 15,scale = 2)
	private BigDecimal amount;
	private String description;

	public String getFormattedPaymentMethod() {
		if (paymentMethod == null) {
			return "";
		}

		String name = paymentMethod.getName();
		return (name != null && !name.isBlank())
				? paymentMethod.getType().name() + " : " + name
				: paymentMethod.getType().name();
	}
}
