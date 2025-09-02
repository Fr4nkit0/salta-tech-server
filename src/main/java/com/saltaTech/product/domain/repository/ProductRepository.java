package com.saltaTech.product.domain.repository;

import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import com.saltaTech.product.domain.persistence.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends SoftDeleteRepository<Product,Long>, JpaSpecificationExecutor<Product> {
}
