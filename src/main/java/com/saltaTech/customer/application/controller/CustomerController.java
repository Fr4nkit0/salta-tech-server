package com.saltaTech.customer.application.controller;

import com.saltaTech.common.domain.dto.response.ErrorResponse;
import com.saltaTech.customer.application.service.interfaces.CustomerService;
import com.saltaTech.customer.domain.dto.request.CustomerCreateRequest;
import com.saltaTech.customer.domain.dto.request.CustomerSearchCriteria;
import com.saltaTech.customer.domain.dto.request.CustomerStatusUpdateRequest;
import com.saltaTech.customer.domain.dto.request.CustomerUpdateRequest;
import com.saltaTech.customer.domain.dto.response.CustomerDetailResponse;
import com.saltaTech.customer.domain.dto.response.CustomerResponse;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("customers")
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Organization")
@Tag(name = "Clientes",description = "Contiene todas las operaciones relacionadas con los clientes de las Organizaciones de la aplicacion.")
public class CustomerController {
	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	@GetMapping
	@Operation(summary = "Listar clientes", description = "Obtiene una lista paginada de clientes con filtros opcionales")
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Clientes encontrados exitosamente."
			),
			@ApiResponse(
					responseCode = "404",
					description = "Recurso no encontrado. Falla en los filtros",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<Page<CustomerResponse>> findAll (Pageable pageable,
														   @Parameter(description = "Nombre del cliente") @RequestParam(required = false, name = "first_name") String firstName,
														   @Parameter(description = "Apellido del cliente") @RequestParam(required = false, name = "last_name") String lastName,
														   @Parameter(description = "Nombre completo") @RequestParam(required = false, name = "name") String name,
														   @Parameter(description = "Correo electrónico") @RequestParam(required = false) String email,
														   @Parameter(description = "Número de teléfono") @RequestParam(required = false, name = "phone_number") String phoneNumber,
														   @Parameter(description = "Documento DNI") @RequestParam(required = false) String dni,
														   @Parameter(description = "CUIL") @RequestParam(required = false) String cuil){
		final var searchCriteria = new CustomerSearchCriteria(
				firstName,
				lastName,
				name,
				email,
				phoneNumber,
				dni,
				cuil
		);
		return ResponseEntity.ok(customerService.findAll(pageable, searchCriteria));
	}
	@GetMapping("/{customerId}")
	@Operation(summary = "Obtener cliente por ID", description = "Devuelve los datos completos de un cliente")
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Cliente encontrado exitosamente.",
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
	public ResponseEntity<CustomerDetailResponse> findById (@Parameter(description = "ID del cliente", example = "1") @PathVariable Long customerId){
		return ResponseEntity.ok(customerService.findById(customerId));
	}
	@PostMapping
	@Operation(summary = "Crear cliente", description = "Crea un nuevo cliente. Algunos campos son opcionales.")
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Cliente creado correctamente",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = CustomerDetailResponse.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Datos inválidos",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public  ResponseEntity<CustomerDetailResponse> create (@Parameter(description = "Datos del cliente a crear") @Valid @RequestBody CustomerCreateRequest customerCreateRequest,
													 HttpServletRequest request){
		final var customerCreated = customerService.create(customerCreateRequest);
		final var baseURl = request.getRequestURL().toString();
		final var newLocation = URI.create(baseURl+"/"+customerCreated.id());
		return  ResponseEntity
					.created(newLocation)
					.body(customerCreated);
	}
	@PutMapping("/{customerId}")
	@Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente")
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Cliente actulizado exitosamente.",
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
	public ResponseEntity<CustomerDetailResponse> update ( @Parameter(description = "ID del cliente a actualizar") @PathVariable Long customerId,
													 @Parameter(description = "Datos del cliente a actualizar") @Valid @RequestBody CustomerUpdateRequest customerUpdateRequest){
		return ResponseEntity.ok(customerService.update(customerUpdateRequest,customerId));
	}
	@DeleteMapping("/{customerId}")
	@Operation(summary = "Eliminar cliente", description = "Elimina un cliente por ID")
	@ApiResponses({
			@ApiResponse(
					responseCode = "204",
					description = "Cliente eliminado correctamente."
			),
			@ApiResponse(
					responseCode = "404",
					description = "Recurso no encontrado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<Void> delete (@Parameter(description = "ID del cliente a eliminar") @PathVariable Long customerId){
		customerService.deleteById(customerId);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{customerId}/status")
	@Operation(summary = "Actualizar estado de cliente", description = "Actualiza el estado de un cliente")
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Estado del cliente actulizado correctamente."
			),
			@ApiResponse(
					responseCode = "404",
					description = "Recurso no encontrado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<Void> updateStatus(@Parameter(description = "ID del cliente") @PathVariable Long customerId,
											 @Parameter(description = "Nuevo estado del cliente") @Valid @RequestBody CustomerStatusUpdateRequest updateRequest) {
		customerService.updateStatus(customerId, updateRequest);
		return ResponseEntity.ok().build();
	}
}
