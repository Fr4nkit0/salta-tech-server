package com.saltaTech.sale.application.service.interfaces;

import com.saltaTech.sale.domain.dto.request.SaleCreateRequest;
import com.saltaTech.sale.domain.dto.request.SaleSearchCriteria;
import com.saltaTech.sale.domain.dto.response.SalesDetailsResponse;
import com.saltaTech.sale.domain.dto.response.SaleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SaleService {
	SalesDetailsResponse create (SaleCreateRequest createRequest);
    Page<SaleResponse> findAll (Pageable pageable, SaleSearchCriteria searchCriteria);
    SalesDetailsResponse findById (Long id);
}
