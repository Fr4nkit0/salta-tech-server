package com.saltaTech.product.application.service.implementations;

import com.saltaTech.auth.application.security.authentication.context.BranchContext;
import com.saltaTech.brand.domain.repository.BrandRepository;
import com.saltaTech.category.domain.repository.CategoryRepository;
import com.saltaTech.common.application.aop.BranchSecured;
import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.branch.domain.repository.BranchRepository;
import com.saltaTech.product.application.exceptions.NoProductsFoundException;
import com.saltaTech.product.application.exceptions.ProductNotFoundException;
import com.saltaTech.product.application.mapper.ProductMapper;
import com.saltaTech.product.application.service.interfaces.ProductService;
import com.saltaTech.product.domain.dto.request.ProductCreateRequest;
import com.saltaTech.product.domain.dto.request.ProductSearchCriteria;
import com.saltaTech.product.domain.dto.request.ProductUpdateRequest;
import com.saltaTech.product.domain.dto.response.ProductDetailResponse;
import com.saltaTech.product.domain.dto.response.ProductResponse;
import com.saltaTech.product.domain.persistence.Product;
import com.saltaTech.product.domain.repository.ProductRepository;
import com.saltaTech.product.domain.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
@BranchSecured
public class ProductServiceImpl implements ProductService {
	private final ProductMapper productMapper;
	private final ProductRepository productRepository;
	private final BranchRepository branchRepository;
	private final BrandRepository brandRepository;
	private final CategoryRepository categoryRepository;

	public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository, BranchRepository branchRepository, CategoryRepository categoryRepository, BrandRepository brandRepository) {
		this.productMapper = productMapper;
		this.productRepository = productRepository;
		this.branchRepository = branchRepository;
		this.brandRepository = brandRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Page<ProductResponse> findAll(Pageable pageable, ProductSearchCriteria searchCriteria) {
		var productSpecification = new ProductSpecification(searchCriteria);
		var products = productRepository.findAll(productSpecification,pageable);
		if (products.isEmpty()) {
			throw new NoProductsFoundException(searchCriteria);
		}
		return products.map(productMapper::toProductResponse);
	}

	@Override
	public ProductDetailResponse findById(Long id) {
		return productMapper.toProductDetailResponse(findByIdEntity(id));
	}

	@Override
	public ProductDetailResponse create(ProductCreateRequest createRequest) {
		final var tenant = BranchContext.getBranchTenant() ;
		final var organization = branchRepository
				.findActiveByTenant(tenant)
				.orElseThrow(()-> new BranchNotFoundException(tenant)) ;
		final var category = categoryRepository.findById(createRequest.categoryId()).orElse(null);
		final var brand = brandRepository.findById(createRequest.brandId()).orElse(null);
		final var product = productRepository.save(productMapper.toProduct(createRequest, organization, category, brand));
		return productMapper.toProductDetailResponse(product);
	}

	@Override
	public ProductDetailResponse update(Long id, ProductUpdateRequest updateRequest) {
		var oldProduct = findByIdEntity(id);
		final var newCategory = categoryRepository.findById(updateRequest.categoryId()).orElse(null);
		final var newBrand = brandRepository.findById(updateRequest.brandId()).orElse(null);
		productMapper.toUpdateProduct(oldProduct,newCategory,newBrand, updateRequest);
		final var productUpdated = productRepository.save(oldProduct);
		return productMapper.toProductDetailResponse(productUpdated);
	}

	@Override
	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}
	private Product findByIdEntity (Long id){
		return productRepository.findById(id)
				.orElseThrow(()-> new ProductNotFoundException(id));
	}
}
