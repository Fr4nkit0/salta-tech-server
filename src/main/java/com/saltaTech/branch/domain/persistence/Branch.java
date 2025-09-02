package com.saltaTech.branch.domain.persistence;

import com.saltaTech.common.domain.persistence.Auditable;
import com.saltaTech.organization.domain.persistence.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "branches")
@EntityListeners(AuditingEntityListener.class)
public class Branch  extends Auditable {
	@Column(nullable = false, length = 100)
	private String name;
	/*@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "address_id")
	private Address address; */
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name= "organization_id", nullable = false)
	private Organization organization;

}
