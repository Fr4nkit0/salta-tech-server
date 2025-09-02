package com.saltaTech.category.domain.repository;

import com.saltaTech.category.domain.persistence.Category;
import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends SoftDeleteRepository<Category,Long>, JpaSpecificationExecutor<Category> {
}
