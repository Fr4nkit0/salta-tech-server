package com.saltaTech.product.application.service.interfaces;

import com.saltaTech.product.domain.dto.request.ProductCreateRequest;
import com.saltaTech.product.domain.dto.request.ProductSearchCriteria;
import com.saltaTech.product.domain.dto.request.ProductUpdateRequest;
import com.saltaTech.product.domain.dto.response.ProductDetailResponse;
import com.saltaTech.product.domain.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
	Page<ProductResponse> findAll (Pageable pageable, ProductSearchCriteria searchCriteria);
	ProductDetailResponse findById (Long id);
	ProductDetailResponse create (ProductCreateRequest createRequest);
	ProductDetailResponse update (Long id, ProductUpdateRequest updateRequest);
	void deleteById (Long id);
}
