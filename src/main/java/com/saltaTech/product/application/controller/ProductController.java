package com.saltaTech.product.application.controller;

import com.saltaTech.common.domain.dto.response.ErrorResponse;
import com.saltaTech.customer.domain.dto.response.CustomerDetailResponse;
import com.saltaTech.product.application.service.interfaces.ProductService;
import com.saltaTech.product.domain.dto.request.ProductCreateRequest;
import com.saltaTech.product.domain.dto.request.ProductSearchCriteria;
import com.saltaTech.product.domain.dto.request.ProductUpdateRequest;
import com.saltaTech.product.domain.dto.response.ProductDetailResponse;
import com.saltaTech.product.domain.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("products")
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Branch")
@Tag(name = "Productos",description = "Contiene todas las operaciones relacionadas con los productos de las sucursales de la aplicacion.")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	@GetMapping
	@Operation(summary = "Listar los Productos", description = "Obtiene una lista paginada de los productos con filtros opcionales")
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Productos encontradas exitosamente.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Page.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Recurso no encontrado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<Page<ProductResponse>> findAll (Pageable pageable,
														  @Parameter(description = "Nombre del producto") @RequestParam(required = false) String name,
														  @Parameter(description = "Precio del producto") @RequestParam(required = false) BigDecimal price,
														  @Parameter(description = "Nombre de la categoria") @RequestParam(required = false) String category,
														  @Parameter(description = "Nombre de la marca ") @RequestParam(required = false) String brand) {
		var searchCriteria = new ProductSearchCriteria(name, price, category, brand);
		return ResponseEntity.ok(productService.findAll(pageable, searchCriteria));
	}
	@GetMapping({"/{productId}"})
	@Operation(summary = "Obtener producto por ID", description = "Obtiene el detalle de un producto por su ID")
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Productos encontrado exitosamente.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ProductDetailResponse.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Recurso no encontrado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<ProductDetailResponse> findById (@PathVariable Long productId) {
		return ResponseEntity.ok(productService.findById(productId));
	}
	@PostMapping
	@Operation(summary = "Crear producto", description = "Crea un nuevo producto, el campo de category_id y brand_id son opciones los demas son todos obligatorios.")
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Producto creado correctamente",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ProductDetailResponse.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Datos inválidos",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<ProductDetailResponse> create( @Parameter(description = "Datos del producto ha crear ")@Valid  @RequestBody ProductCreateRequest createRequest,
														HttpServletRequest request) {
		final var savedProduct = productService.create(createRequest);
		final var baseURl = request.getRequestURL().toString();
		final var newLocation = URI.create(baseURl+"/"+savedProduct.id());
		return ResponseEntity.created(newLocation).body(savedProduct);
	}
	@PutMapping({"/{productId}"})
	@Operation(summary = "Actualizar producto", description = "Actualiza los datos de un Producto existente")
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Producto actulizado exitosamente.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = CustomerDetailResponse.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Recurso no encontrado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<ProductDetailResponse> update( @Parameter(description = "ID del producto a actualizar")@PathVariable Long productId,
														 @Parameter(description = "Datos del producto a actualizar")@Valid @RequestBody ProductUpdateRequest updateRequest) {
		return ResponseEntity.ok(productService.update(productId, updateRequest));
	}
	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> delete(@PathVariable Long productId) {
		productService.deleteById(productId);
		return ResponseEntity.noContent().build();
	}
}
