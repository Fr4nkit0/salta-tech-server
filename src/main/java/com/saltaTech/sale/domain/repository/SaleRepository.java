package com.saltaTech.sale.domain.repository;

import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import com.saltaTech.sale.domain.persistence.Sale;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends SoftDeleteRepository<Sale,Long> {
}
