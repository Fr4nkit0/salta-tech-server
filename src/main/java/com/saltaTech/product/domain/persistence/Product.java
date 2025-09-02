package com.saltaTech.product.domain.persistence;

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

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Filter(
		name = Filters.ORGANIZATION_FILTER,
		condition = "EXISTS (SELECT 1 FROM organizations o WHERE "+
				"o.id = organization_id AND o.slug = :" + Filters.ORGANIZATION_SLUG_PARAM + ")"
)
public class Product extends OrganizationFilterDefinition {
	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	@Column(length = 50,nullable = false)
	private String name;
	@Column(length = 500, nullable = false)
	private String description;
	@Column(nullable = false)
	private Integer availableQuantity;
	@Column(precision = 15,scale = 2,nullable = false)
	private BigDecimal price;

}
