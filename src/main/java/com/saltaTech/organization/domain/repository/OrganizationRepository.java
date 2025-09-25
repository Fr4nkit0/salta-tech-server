package com.saltaTech.organization.domain.repository;

import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import com.saltaTech.organization.domain.persistence.Organization;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository

public interface OrganizationRepository extends SoftDeleteRepository<Organization,Long>, JpaSpecificationExecutor<Organization> {
	@Query(
			"SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Organization o " +
					"WHERE o.slug =?1 AND o.enabled = true")
	boolean existsActiveByTenant(String tenant);
	@Query("SELECT o FROM Organization o WHERE o.enabled = true AND o.slug = ?1")
	Optional<Organization> findActiveByTenant(String tenant);
}
