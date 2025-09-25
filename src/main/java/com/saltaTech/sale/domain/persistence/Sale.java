package com.saltaTech.sale.domain.persistence;

import com.saltaTech.common.domain.persistence.Filters;
import com.saltaTech.common.domain.persistence.OrganizationFilterDefinition;
import com.saltaTech.customer.domain.persistence.Customer;
import com.saltaTech.payment.domain.persistence.Payment;
import com.saltaTech.sale.domain.util.SaleStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "sales")
@EntityListeners(AuditingEntityListener.class)
@Filter(
		name = Filters.ORGANIZATION_FILTER,
		condition = "EXISTS (SELECT 1 FROM organizations o WHERE "+
				"o.id = organization_id AND o.slug = :" + Filters.ORGANIZATION_SLUG_PARAM + ")"
)
public class Sale extends OrganizationFilterDefinition {
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false,length = 40)
	private SaleStatus status;
	@Column(nullable = false,precision = 15,scale = 2)
	private BigDecimal total;
	@OneToMany(
			cascade = {CascadeType.PERSIST,CascadeType.MERGE},
			fetch = FetchType.EAGER,
			mappedBy = "sale")
	private List<Payment> payments;
	@OneToMany(
			cascade = {CascadeType.PERSIST,CascadeType.MERGE},
			fetch = FetchType.EAGER,
			mappedBy = "sale")
	private List<SaleDetails> saleDetails;
}
