package com.saltaTech.common.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface SoftDeleteRepository <T,ID> extends JpaRepository<T,ID> {
	@Query("select e from #{#entityName} e where e.enabled = true")
	List<T> findAll();

	@Query("select e from #{#entityName} e where e.enabled = true")
	Page<T> findAll(Pageable pageable);

	@Query("select e from #{#entityName} e where e.enabled = true and e.id = ?1")
	Optional<T> findById(ID id);

	@Query("select e from #{#entityName} e where e.enabled = false")
	List<T> findAllDeleted();

	@Modifying
	@Query("update #{#entityName} e set e.enabled = false, e.deletedDate = CURRENT_TIMESTAMP where e.id = ?1")
	void deleteById(ID id);

}
