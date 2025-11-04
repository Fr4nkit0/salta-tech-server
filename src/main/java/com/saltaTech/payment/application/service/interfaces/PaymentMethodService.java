package com.saltaTech.payment.application.service.interfaces;

import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.payment.application.exceptions.NoPaymentsFoundException;
import com.saltaTech.payment.application.exceptions.PaymentMethodFoundException;
import com.saltaTech.payment.domain.dto.request.PaymentMethodUpdateRequest;
import com.saltaTech.payment.domain.dto.request.PaymentMethodCreateRequest;
import com.saltaTech.payment.domain.dto.request.PaymentMethodSearchCriteria;
import com.saltaTech.payment.domain.dto.response.PaymentMethodResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio para gestionar métodos de pago dentro de la organización.
 * Todos los métodos aseguran que las operaciones respeten la organización
 * actual según el contexto de ejecución.
 */
public interface PaymentMethodService  {
	/**
	 * Obtiene una página de métodos de pago según los criterios de búsqueda proporcionados.
	 *
	 * @param pageable       Información de paginación (página, tamaño, ordenamiento).
	 * @param searchCriteria Criterios de filtrado de métodos de pago.
	 * @return Una página de {@link PaymentMethodResponse} que cumple con los criterios.
	 * @throws NoPaymentsFoundException Si no se encuentra ningún método de pago con los criterios dados.
	 */
	Page<PaymentMethodResponse> findAll(Pageable pageable, PaymentMethodSearchCriteria searchCriteria);
	/**
	 * Busca un método de pago por su ID.
	 *
	 * @param id ID del método de pago.
	 * @return El método de pago encontrado como {@link PaymentMethodResponse}.
	 * @throws PaymentMethodFoundException Si no se encuentra un método de pago con el ID dado.
	 */
	PaymentMethodResponse findById(Long id);
	/**
	 * Crea un nuevo método de pago dentro de la organización actual.
	 *
	 * @param createRequest Datos necesarios para crear el método de pago.
	 * @return El método de pago creado como {@link PaymentMethodResponse}.
	 * @throws BranchNotFoundException Si no se encuentra la organización actual.
	 */
	PaymentMethodResponse create(PaymentMethodCreateRequest createRequest);
	/**
	 * Actualiza un método de pago existente.
	 *
	 * @param updateRequest Datos a actualizar del método de pago.
	 * @param id            ID del método de pago a actualizar.
	 * @return El método de pago actualizado como {@link PaymentMethodResponse}.
	 * @throws PaymentMethodFoundException Si no se encuentra un método de pago con el ID dado.
	 */
	PaymentMethodResponse update(PaymentMethodUpdateRequest updateRequest, Long id);
	/**
	 * Elimina un método de pago por su ID.
	 *
	 * @param id ID del método de pago a eliminar.
	 * @throws PaymentMethodFoundException Si no se encuentra un método de pago con el ID dado.
	 */
	void deleteById(Long id);
}
