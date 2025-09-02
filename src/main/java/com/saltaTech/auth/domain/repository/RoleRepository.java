package com.saltaTech.auth.domain.repository;

import com.saltaTech.auth.domain.persistence.Role;
import com.saltaTech.organization.domain.persistence.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Buscar rol por nombre y organización
    Optional<Role> findByNameAndOrganization(String name, Organization organization);
}