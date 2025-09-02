package com.saltaTech.branch.domain.repository;

import com.saltaTech.branch.domain.persistence.Branch;
import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends SoftDeleteRepository<Branch,Long>, JpaSpecificationExecutor<Branch> {
	@Query("""
       SELECT b 
       FROM Branch b 
       WHERE b.id = ?1 
         AND b.organization.slug = ?2 
         AND b.enabled = true
       """)
	Optional<Branch> findEnabledByIdAndOrganizationSlug(Long id, String slug);
}
