package com.saltaTech.product.application.mapper;

import com.saltaTech.branch.domain.persistence.Branch;
import com.saltaTech.organization.domain.persistence.Organization;
import com.saltaTech.product.domain.persistence.BranchStock;
import com.saltaTech.product.domain.persistence.Product;
import org.springframework.stereotype.Service;

@Service
public class BranchStockMapper {
	public BranchStock toBranchStock(Organization organization, Branch branch, Product product, Integer quantity) {
		return BranchStock.builder()
				.organization(organization)
				.branch(branch)
				.product(product)
				.quantity(quantity)
				.build();
	}
}
