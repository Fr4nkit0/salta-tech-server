package com.saltaTech.category.domain.persistence;

import com.saltaTech.common.domain.persistence.Filters;
import com.saltaTech.common.domain.persistence.OrganizationFilterDefinition;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Table(name = "categories")
@EntityListeners(AuditingEntityListener.class)
@Filter(
		name = Filters.ORGANIZATION_FILTER,
		condition = "EXISTS (SELECT 1 FROM organizations o WHERE "+
				"o.id = organization_id AND o.slug = :" + Filters.ORGANIZATION_SLUG_PARAM + ")"
)
public class Category extends OrganizationFilterDefinition  {
	private String name ;
}
