package com.saltaTech.product.application.mapper;

import com.saltaTech.brand.application.mapper.BrandMapper;
import com.saltaTech.brand.domain.persistence.Brand;
import com.saltaTech.category.application.mapper.CategoryMapper;
import com.saltaTech.category.domain.persistence.Category;
import com.saltaTech.organization.domain.persistence.Organization;
import com.saltaTech.product.domain.dto.request.ProductCreateRequest;
import com.saltaTech.product.domain.dto.request.ProductUpdateRequest;
import com.saltaTech.product.domain.dto.response.ProductDetailResponse;
import com.saltaTech.product.domain.dto.response.ProductResponse;
import com.saltaTech.product.domain.persistence.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
	private final CategoryMapper categoryMapper;
	private final BrandMapper brandMapper;

	public ProductMapper(BrandMapper brandMapper, CategoryMapper categoryMapper) {
		this.brandMapper = brandMapper;
		this.categoryMapper = categoryMapper;
	}

	public Product toProduct (ProductCreateRequest createRequest, Organization organization, Category category, Brand brand){
		return Product.builder()
				.organization(organization)
				.category(category)
				.brand(brand)
				.name(createRequest.name())
				.description(createRequest.description())
				.price(createRequest.price())
				.availableQuantity(createRequest.quantity())
				.build();
	}

	public ProductResponse toProductResponse (Product product){
		return new ProductResponse(
				product.getId(),
				product.getCategory().getName(),
				product.getBrand().getName(),
				product.getName(),
				product.getPrice(),
				product.getAvailableQuantity()
		);
	}
	public ProductDetailResponse toProductDetailResponse (Product product){
		return new ProductDetailResponse(
				product.getId(),
				categoryMapper.toCategoryResponse(product.getCategory()),
				brandMapper.toBrandResponse(product.getBrand()),
				product.getName(),
				product.getPrice(),
				product.getAvailableQuantity()
		);
	}
	public void toUpdateProduct(Product oldProduct, Category newCategory, Brand newBrand,ProductUpdateRequest updateRequest){
		oldProduct.setCategory(newCategory);
		oldProduct.setBrand(newBrand);
		oldProduct.setName(updateRequest.name());
		oldProduct.setDescription(updateRequest.description());
		oldProduct.setPrice(updateRequest.price());
		oldProduct.setAvailableQuantity(updateRequest.quantity());
	}
}
