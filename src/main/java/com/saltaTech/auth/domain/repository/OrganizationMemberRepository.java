package com.saltaTech.auth.domain.repository;

import com.saltaTech.auth.domain.persistence.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember,Long> {


	@Query("SELECT om FROM OrganizationMember om " +
			"JOIN FETCH om.branchAccesses ba " +
			"WHERE om.user.email = ?1 " +          // <- espacio al final
			"AND om.user.enabled = true " +
			"AND om.organization.slug = ?2 " +      // <- espacio al final
			"AND om.organization.enabled = true")
	Optional<OrganizationMember> findByUserEmailAndOrganizationSlug(String email, String slug);
}
