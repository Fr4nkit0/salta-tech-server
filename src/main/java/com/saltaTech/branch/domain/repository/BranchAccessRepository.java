package com.saltaTech.branch.domain.repository;

import com.saltaTech.branch.domain.persistence.BranchAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchAccessRepository extends JpaRepository<BranchAccess,Long> {
	@Query("SELECT CASE WHEN COUNT(ba) > 0 THEN TRUE ELSE FALSE END FROM BranchAccess ba WHERE ba.organizationMember.id = ?1 AND ba.branch.id = ?2 AND ba.branch.enabled = true")
	boolean existsByOrganizationMemberIdAndBranchIdAndEnabledTrue (Long organizationMemberId, Long branchId);

}
