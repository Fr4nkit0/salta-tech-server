package com.saltaTech.product.domain.persistence;

import com.saltaTech.branch.domain.persistence.Branch;
import com.saltaTech.brand.domain.persistence.Brand;
import com.saltaTech.category.domain.persistence.Category;
import com.saltaTech.common.domain.persistence.Filters;
import com.saltaTech.common.domain.persistence.OrganizationFilterDefinition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Table(name = "branch_stocks")
@EntityListeners(AuditingEntityListener.class)
@Filter(
		name = Filters.ORGANIZATION_FILTER,
		condition = "EXISTS (SELECT 1 FROM organizations o WHERE "+
				"o.id = organization_id AND o.slug = :" + Filters.ORGANIZATION_SLUG_PARAM + ")"
)
public class BranchStock extends OrganizationFilterDefinition {
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	@ManyToOne
	@JoinColumn(name = "branch_id")
	private Branch branch;
	@Column(nullable = false)
	private Integer quantity;
}
