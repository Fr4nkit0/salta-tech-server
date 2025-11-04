package com.saltaTech.auth.domain.repository;

import com.saltaTech.auth.domain.persistence.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}