package com.saltaTech.auth.domain.repository;

import com.saltaTech.auth.domain.persistence.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember,Long> {


	@Query("SELECT om FROM OrganizationMember om " +
			"WHERE om.user.email = ?1 " +
			"AND om.user.enabled = true " +
			"AND om.organization.slug = ?2 " +
			"AND om.organization.enabled = true")
	Optional<OrganizationMember> findByUserEmailAndTenant(String email, String slug);
}
