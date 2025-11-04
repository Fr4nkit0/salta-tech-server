package com.saltaTech.branch.domain.repository;

import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import com.saltaTech.branch.domain.persistence.Branch;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository

public interface BranchRepository extends SoftDeleteRepository<Branch,Long>, JpaSpecificationExecutor<Branch> {
	@Query(
			"SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END FROM Branch b " +
					"WHERE b.identifier =?1 AND b.enabled = true")
	boolean existsActiveByTenant(String tenant);
	@Query("SELECT b FROM Branch b WHERE b.enabled = true AND b.identifier = ?1")
	Optional<Branch> findActiveByTenant(String tenant);
}
