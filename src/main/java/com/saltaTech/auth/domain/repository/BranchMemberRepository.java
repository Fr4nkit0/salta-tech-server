package com.saltaTech.auth.domain.repository;

import com.saltaTech.auth.domain.persistence.BranchMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchMemberRepository extends JpaRepository<BranchMember,Long> {


	@Query("SELECT bm FROM BranchMember bm " +
			"WHERE bm.user.email = ?1 " +
			"AND bm.user.enabled = true " +
			"AND bm.branch.identifier = ?2 " +
			"AND bm.branch.enabled = true")
	Optional<BranchMember> findByUserEmailAndTenant(String email, String identifier);
}
