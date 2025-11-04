package com.saltaTech.sale.domain.persistence;

import com.saltaTech.common.domain.persistence.Filters;
import com.saltaTech.common.domain.persistence.BranchFilterDefinition;
import com.saltaTech.product.domain.persistence.Product;
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
@Table(name = "sales_details")
@EntityListeners(AuditingEntityListener.class)
@Filter(
		name = Filters.BRANCH_FILTER,
		condition = "EXISTS (SELECT 1 FROM branches b WHERE "+
				"b.id = branch_id AND b.identifier = :" + Filters.BRANCH_SLUG_PARAM + ")"
)
public class SaleDetails extends BranchFilterDefinition {
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "sale_id", nullable = false)
	private Sale sale;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	@Column(nullable = false)
	private Integer quantity;
	@Column(nullable = false,precision = 15,scale = 2)
	private BigDecimal price;
}
