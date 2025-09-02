package com.saltaTech.brand.domain.repository;

import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import com.saltaTech.brand.domain.persistence.Brand;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends SoftDeleteRepository<Brand,Long>, JpaSpecificationExecutor<Brand> {
}
