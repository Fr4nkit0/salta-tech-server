package com.saltaTech.sale.domain.repository;

import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import com.saltaTech.sale.domain.persistence.SaleDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesDetailsRepository extends SoftDeleteRepository<SaleDetails,Long> {
}
