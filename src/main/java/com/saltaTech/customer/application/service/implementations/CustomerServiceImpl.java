package com.saltaTech.customer.application.service.implementations;

import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.common.application.aop.OrganizationSecured;
import com.saltaTech.customer.domain.dto.request.CustomerUpdateRequest;
import com.saltaTech.customer.application.exceptions.CustomerNotFoundException;
import com.saltaTech.customer.application.exceptions.NoCustomersException;
import com.saltaTech.customer.application.mapper.CustomerMapper;
import com.saltaTech.customer.application.service.interfaces.CustomerService;
import com.saltaTech.customer.domain.dto.request.CustomerCreateRequest;
import com.saltaTech.customer.domain.dto.request.CustomerSearchCriteria;
import com.saltaTech.customer.domain.dto.request.CustomerStatusUpdateRequest;
import com.saltaTech.customer.domain.dto.response.CustomerDetailResponse;
import com.saltaTech.customer.domain.dto.response.CustomerResponse;
import com.saltaTech.customer.domain.persistence.Customer;
import com.saltaTech.customer.domain.repository.CustomerRepository;
import com.saltaTech.customer.domain.specification.CustomerSpecification;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import com.saltaTech.organization.domain.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@OrganizationSecured
@Transactional
public class CustomerServiceImpl implements CustomerService {
	private final CustomerRepository customerRepository ;
	private final CustomerMapper customerMapper;
	private final OrganizationRepository organizationRepository;

	public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository, OrganizationRepository organizationRepository) {
		this.customerMapper = customerMapper;
		this.customerRepository = customerRepository;
		this.organizationRepository = organizationRepository;
	}

	@Transactional(readOnly = true)
	public Page<CustomerResponse> findAll(Pageable pageable, CustomerSearchCriteria searchCriteria) {
		final var customerSpecification = new CustomerSpecification(searchCriteria);
		var customers = customerRepository.findAll(customerSpecification, pageable);
		if (customers.isEmpty()) {
			throw new NoCustomersException(searchCriteria);
		}
		log.debug("Criterios de búsqueda: [{}]", searchCriteria);
		return customers.map(customerMapper::toCustomerResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public CustomerDetailResponse findById(Long id) {
		return customerMapper.toCustomerDetailResponse(findByIdEntity(id));
	}

	@Override
	public CustomerDetailResponse create(CustomerCreateRequest customerCreateRequest) {
		final var tenant = OrganizationContext.getOrganizationTenant();
		final var organization = organizationRepository.findActiveByTenant(tenant)
				.orElseThrow(()-> new OrganizationNotFoundException(tenant));
		var createCustomer = customerMapper.toCustomer(customerCreateRequest,organization);
		return customerMapper.toCustomerDetailResponse(
				customerRepository.save(createCustomer)
		);
	}

	@Override
	public CustomerDetailResponse update(CustomerUpdateRequest updateRequest, Long id) {
		var oldCustomer = findByIdEntity(id);
		customerMapper.toUpdateCustomer(oldCustomer, updateRequest);
		var updateCustomer = customerRepository.save(oldCustomer);
		return customerMapper.toCustomerDetailResponse(updateCustomer);
	}

	@Override
	public void deleteById(Long id) {
		customerRepository.deleteById(id);
	}

	@Override
	public void updateStatus(Long customerId, CustomerStatusUpdateRequest updateRequest){
		Customer customer = findByIdEntity(customerId);
		customer.setStatus(updateRequest.status());
		customerRepository.save(customer);
	}

	private Customer findByIdEntity (Long id){
		return  customerRepository.findById(id)
				.orElseThrow(()->  new CustomerNotFoundException(id));
	}
}
