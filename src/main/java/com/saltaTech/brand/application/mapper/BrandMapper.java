package com.saltaTech.brand.application.mapper;

import com.saltaTech.brand.domain.dto.request.BrandCreateRequest;
import com.saltaTech.brand.domain.dto.request.BrandUpdateRequest;
import com.saltaTech.brand.domain.dto.response.BrandResponse;
import com.saltaTech.brand.domain.persistence.Brand;
import com.saltaTech.organization.domain.persistence.Organization;
import org.springframework.stereotype.Service;

@Service
public class BrandMapper {
    public Brand toBrand(BrandCreateRequest createRequest, Organization organization){
        if(createRequest == null) return null;
        return Brand.builder()
                .organization(organization)
                .name(createRequest.name())
                .build();
    }

    public BrandResponse toBrandResponse(Brand brand){
        if(brand == null) return null;
        return new BrandResponse(
                brand.getId(),
                brand.getName()
        );
    }

    public void toUpdateBrand(Brand oldBrand, BrandUpdateRequest updateRequest){
        if(updateRequest.name() != null){
            oldBrand.setName(updateRequest.name());
        }
    }
}
