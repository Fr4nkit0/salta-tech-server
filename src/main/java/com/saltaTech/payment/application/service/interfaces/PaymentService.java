package com.saltaTech.payment.application.service.interfaces;

import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.payment.application.exceptions.NoPaymentsFoundException;
import com.saltaTech.payment.application.exceptions.PaymentMethodFoundException;
import com.saltaTech.payment.application.exceptions.PaymentNotFoundException;
import com.saltaTech.payment.domain.dto.request.PaymentCreateRequest;
import com.saltaTech.payment.domain.dto.request.PaymentSearchCriteria;
import com.saltaTech.payment.domain.dto.request.PaymentUpdateRequest;
import com.saltaTech.payment.domain.dto.response.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio para gestionar pagos dentro de la organización.
 * Todos los métodos aseguran que las operaciones respeten la organización
 * actual según el contexto de ejecución.
 */
public interface PaymentService {

	/**
	 * Obtiene una página de pagos según los criterios de búsqueda proporcionados.
	 *
	 * @param pageable       Información de paginación (página, tamaño, ordenamiento).
	 * @param searchCriteria Criterios de filtrado de pagos.
	 * @return Una página de {@link PaymentResponse} que cumple con los criterios.
	 * @throws NoPaymentsFoundException Si no se encuentra ningún pago con los criterios dados.
	 */
	Page<PaymentResponse> findAll(Pageable pageable, PaymentSearchCriteria searchCriteria);

	/**
	 * Busca un pago por su ID.
	 *
	 * @param id ID del pago.
	 * @return El pago encontrado como {@link PaymentResponse}.
	 * @throws PaymentNotFoundException Si no se encuentra un pago con el ID dado.
	 */
	PaymentResponse findById(Long id);

	/**
	 * Crea un nuevo pago dentro de la organización actual.
	 *
	 * @param createRequest Datos necesarios para crear el pago.
	 * @return El pago creado como {@link PaymentResponse}.
	 * @throws BranchNotFoundException Si no se encuentra la organización actual.
	 * @throws PaymentMethodFoundException   Si no se encuentra el método de pago indicado.
	 */
	PaymentResponse create(PaymentCreateRequest createRequest);

	/**
	 * Actualiza un pago existente.
	 *
	 * @param updateRequest Datos a actualizar del pago.
	 * @param id            ID del pago a actualizar.
	 * @return El pago actualizado como {@link PaymentResponse}.
	 * @throws PaymentNotFoundException Si no se encuentra un pago con el ID dado.
	 */
	PaymentResponse update(PaymentUpdateRequest updateRequest, Long id);

	/**
	 * Elimina un pago por su ID.
	 *
	 * @param id ID del pago a eliminar.
	 * @throws PaymentNotFoundException Si no se encuentra un pago con el ID dado.
	 */
	void deleteById(Long id);
}
