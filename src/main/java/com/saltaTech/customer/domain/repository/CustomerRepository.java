package com.saltaTech.customer.domain.repository;

import com.saltaTech.common.domain.repository.SoftDeleteRepository;
import com.saltaTech.customer.domain.persistence.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends SoftDeleteRepository<Customer,Long>, JpaSpecificationExecutor<Customer> {
}
