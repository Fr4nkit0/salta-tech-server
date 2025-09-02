package com.saltaTech.payment.domain.repository;

import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import com.saltaTech.payment.domain.persistence.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends SoftDeleteRepository<Transaction,Long> {
}
