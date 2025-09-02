package com.saltaTech.payment.domain.repository;

import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import com.saltaTech.payment.domain.persistence.PaymentMethod;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends SoftDeleteRepository<PaymentMethod,Long>, JpaSpecificationExecutor<PaymentMethod> {
}
