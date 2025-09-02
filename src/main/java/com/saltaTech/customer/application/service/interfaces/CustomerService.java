package com.saltaTech.customer.application.service.interfaces;

import com.saltaTech.customer.application.exceptions.CustomerNotFoundException;
import com.saltaTech.customer.application.exceptions.NoCustomersException;
import com.saltaTech.customer.domain.dto.request.CustomerCreateRequest;
import com.saltaTech.customer.domain.dto.request.CustomerSearchCriteria;
import com.saltaTech.customer.domain.dto.request.CustomerUpdateRequest;
import com.saltaTech.customer.domain.dto.request.CustomerStatusUpdateRequest;
import com.saltaTech.customer.domain.dto.response.CustomerDetailResponse;
import com.saltaTech.customer.domain.dto.response.CustomerResponse;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio para gestionar clientes dentro de una organización.
 * Todos los métodos aseguran que las operaciones respeten la organización
 * actual según el contexto de ejecución.
 */
public interface CustomerService {

	/**
	 * Obtiene una página de clientes según los criterios de búsqueda proporcionados.
	 *
	 * @param pageable          Información de paginación (página, tamaño, ordenamiento).
	 * @param searchCriteria    Criterios de filtrado de clientes.
	 * @return Una página de {@link CustomerResponse} que cumple con los criterios.
	 * @throws NoCustomersException Si no se encuentra ningún cliente con los criterios dados.
	 */
	Page<CustomerResponse> findAll(Pageable pageable, CustomerSearchCriteria searchCriteria);

	/**
	 * Busca un cliente por su ID.
	 *
	 * @param id  ID del cliente.
	 * @return Detalle completo del cliente como {@link CustomerDetailResponse}.
	 * @throws CustomerNotFoundException Si no se encuentra un cliente con el ID dado.
	 */
	CustomerDetailResponse findById(Long id);

	/**
	 * Crea un nuevo cliente dentro de la organización actual.
	 *
	 * @param customerCreateRequest Datos necesarios para crear el cliente.
	 * @return El cliente creado como {@link CustomerDetailResponse}.
	 * @throws OrganizationNotFoundException Si la organización actual no existe.
	 */
	CustomerDetailResponse create(CustomerCreateRequest customerCreateRequest);

	/**
	 * Actualiza un cliente existente.
	 *
	 * @param updateRequest Datos a actualizar del cliente.
	 * @param id            ID del cliente a actualizar.
	 * @return El cliente actualizado como {@link CustomerDetailResponse}.
	 * @throws CustomerNotFoundException Si no se encuentra un cliente con el ID dado.
	 */
	CustomerDetailResponse update(CustomerUpdateRequest updateRequest, Long id);

	/**
	 * Elimina un cliente por su ID.
	 *
	 * @param id ID del cliente a eliminar.
	 * @throws CustomerNotFoundException Si no se encuentra un cliente con el ID dado.
	 */
	void deleteById(Long id);

	/**
	 * Actualiza el estado de un cliente.
	 *
	 * @param customerId    ID del cliente cuyo estado será actualizado.
	 * @param updateRequest Nuevo estado del cliente.
	 * @throws CustomerNotFoundException Si no se encuentra un cliente con el ID dado.
	 */
	void updateStatus(Long customerId, CustomerStatusUpdateRequest updateRequest);
}
