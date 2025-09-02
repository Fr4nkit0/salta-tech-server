package com.saltaTech.auth.domain.repository;

import com.saltaTech.auth.domain.persistence.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
	@Query("SELECT o FROM Operation o WHERE o.permitAll=true")
	List<Operation> findByPublicAccess ();
}
