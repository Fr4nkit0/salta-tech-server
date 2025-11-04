package com.saltaTech.sale.application.controller;

import com.saltaTech.common.domain.dto.response.ErrorResponse;
import com.saltaTech.customer.domain.util.Status;
import com.saltaTech.sale.application.service.interfaces.SaleService;
import com.saltaTech.sale.domain.dto.request.SaleCreateRequest;
import com.saltaTech.sale.domain.dto.request.SaleSearchCriteria;
import com.saltaTech.sale.domain.dto.response.SalesDetailsResponse;
import com.saltaTech.sale.domain.dto.response.SaleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequestMapping("sales")
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Branch")
@Tag(name = "Ventas",description = "Contiene todas las operaciones relacionadas con las ventas de las sucursales de la aplicacion.")
public class SaleController {

    private final SaleService saleService;

    public SaleController (SaleService saleService){
        this.saleService = saleService;
    }


	@GetMapping
	@Operation(
			summary = "Listar las ventas",
			description = "Obtiene una lista paginada de las ventas filtradas según los criterios opcionales."
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Ventas encontradas exitosamente.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = SaleResponse.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Recurso no encontrado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<Page<SaleResponse>> findAll(
			Pageable pageable,

			@Parameter(description = "Nombre o apellido del cliente")
			@RequestParam(required = false) String customerName,

			@Parameter(description = "DNI del cliente")
			@RequestParam(required = false) String dni,

			@Parameter(description = "Correo electrónico del cliente")
			@RequestParam(required = false) String email,

			@Parameter(description = "Número de teléfono del cliente")
			@RequestParam(required = false) String phoneNumber,

			@Parameter(description = "Estado de la venta (por ejemplo: PENDING, COMPLETED, CANCELED)")
			@RequestParam(required = false) Status status,

			@Parameter(description = "Monto mínimo total de la venta")
			@RequestParam(required = false) BigDecimal minTotal,

			@Parameter(description = "Monto máximo total de la venta")
			@RequestParam(required = false) BigDecimal maxTotal,

			@Parameter(description = "Fecha inicial del rango de búsqueda (formato: yyyy-MM-dd)")
			@RequestParam(required = false) LocalDate fromDate,

			@Parameter(description = "Fecha final del rango de búsqueda (formato: yyyy-MM-dd)")
			@RequestParam(required = false) LocalDate toDate,

			@Parameter(description = "Filtrar ventas con saldo pendiente (true) o totalmente pagadas (false)")
			@RequestParam(required = false) Boolean hasBalance
	) {

		var searchCriteria = new SaleSearchCriteria(
				customerName,
				dni,
				email,
				phoneNumber,
				status,
				minTotal,
				maxTotal,
				fromDate,
				toDate,
				hasBalance
		);

		return ResponseEntity.ok(saleService.findAll(pageable, searchCriteria));
	}

    @GetMapping("/{saleId}")
	@Operation(summary = "Obtener Venta por ID", description = "Devuelve todos los detalles asociado a una venta")
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Venta encontrada exitosamente.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = SalesDetailsResponse.class))
			),
			@ApiResponse(
					responseCode = "404",
					description = "Recurso no encontrado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
    public ResponseEntity<SalesDetailsResponse> findById (@PathVariable Long saleId){
        return ResponseEntity.ok(saleService.findById(saleId));
    }

	@PostMapping
	@Operation(summary = "Crear Venta", description = "Crea una nueva venta.")
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Venta creada correctamente",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = SalesDetailsResponse.class))
			),
			@ApiResponse(
					responseCode = "400",
					description = "Datos inválidos",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class))
			)
	})
	public ResponseEntity<SalesDetailsResponse> create(@Valid @RequestBody SaleCreateRequest createRequest) {
		return ResponseEntity.ok(saleService.create(createRequest));
	}

}
