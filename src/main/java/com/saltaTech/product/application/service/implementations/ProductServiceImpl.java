package com.saltaTech.product.application.service.implementations;

import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.branch.domain.repository.BranchRepository;
import com.saltaTech.brand.domain.repository.BrandRepository;
import com.saltaTech.category.domain.repository.CategoryRepository;
import com.saltaTech.common.application.aop.OrganizationSecured;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import com.saltaTech.organization.domain.repository.OrganizationRepository;
import com.saltaTech.product.application.exceptions.NoProductsFoundException;
import com.saltaTech.product.application.exceptions.ProductNotFoundException;
import com.saltaTech.product.application.mapper.BranchStockMapper;
import com.saltaTech.product.application.mapper.ProductMapper;
import com.saltaTech.product.application.service.interfaces.ProductService;
import com.saltaTech.product.domain.dto.request.ProductCreateRequest;
import com.saltaTech.product.domain.dto.request.ProductSearchCriteria;
import com.saltaTech.product.domain.dto.request.ProductUpdateRequest;
import com.saltaTech.product.domain.dto.response.ProductDetailResponse;
import com.saltaTech.product.domain.dto.response.ProductResponse;
import com.saltaTech.product.domain.persistence.Product;
import com.saltaTech.product.domain.repository.BranchStockRepository;
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
@OrganizationSecured
public class ProductServiceImpl implements ProductService {
	private final ProductMapper productMapper;
	private final BranchStockMapper branchStockMapper;
	private final ProductRepository productRepository;
	private final BranchRepository branchRepository;
	private final BranchStockRepository branchStockRepository;
	private final OrganizationRepository organizationRepository;
	private final CategoryRepository categoryRepository;
	private final BrandRepository brandRepository;

	public ProductServiceImpl(BranchRepository branchRepository, ProductMapper productMapper, BranchStockMapper branchStockMapper, ProductRepository productRepository, BranchStockRepository branchStockRepository, OrganizationRepository organizationRepository, CategoryRepository categoryRepository, BrandRepository brandRepository) {
		this.branchRepository = branchRepository;
		this.productMapper = productMapper;
		this.branchStockMapper = branchStockMapper;
		this.productRepository = productRepository;
		this.branchStockRepository = branchStockRepository;
		this.organizationRepository = organizationRepository;
		this.categoryRepository = categoryRepository;
		this.brandRepository = brandRepository;
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
		final var slug = OrganizationContext.getOrganizationSlug() ;
		final var organization = organizationRepository
				.findActiveBySlug(slug)
				.orElseThrow(()-> new OrganizationNotFoundException(slug)) ;
		final var branch = branchRepository.findById(createRequest.branchID())
				.orElseThrow(()-> new BranchNotFoundException(createRequest.branchID()));
		final var category = categoryRepository.findById(createRequest.categoryId()).orElse(null);
		final var brand = brandRepository.findById(createRequest.brandId()).orElse(null);
		final var product = productRepository.save(productMapper.toProduct(createRequest, organization, category, brand));
		branchStockRepository.save(branchStockMapper.toBranchStock(organization,branch,product,createRequest.quantity()));
		return productMapper.toProductDetailResponse(product);
	}

	@Override
	public ProductDetailResponse update(Long id, ProductUpdateRequest updateRequest) {
		var oldProduct = findByIdEntity(id);
		final var newCategory = categoryRepository.findById(updateRequest.categoryId()).orElse(null);
		final var newBrand = brandRepository.findById(updateRequest.brandId()).orElse(null);
		final var branch = branchRepository.findById(updateRequest.branchId())
				.orElseThrow(()-> new BranchNotFoundException(updateRequest.branchId()));
		productMapper.toUpdateProduct(oldProduct,newCategory,newBrand, updateRequest);
		final var productUpdated = productRepository.save(oldProduct);
		var oldBranchStock = branchStockRepository.findByBranchIdAndProductId(branch.getId(),productUpdated.getId())
				.orElseThrow(()-> new RuntimeException("El producto no existe en la sucursal"));
		oldBranchStock.setQuantity(updateRequest.quantity());
		branchStockRepository.save(oldBranchStock);
		return productMapper.toProductDetailResponse(productUpdated);
	}

	@Override
	public void deleteById(Long id) {

		productRepository.deleteById(id);
		branchStockRepository.deleteAllByProductId(id);
	}
	private Product findByIdEntity (Long id){
		return productRepository.findById(id)
				.orElseThrow(()-> new ProductNotFoundException(id));
	}
}
