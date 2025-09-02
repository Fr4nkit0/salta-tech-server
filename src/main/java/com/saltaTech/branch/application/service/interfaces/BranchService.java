package com.saltaTech.branch.application.service.interfaces;

import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.branch.application.exceptions.NoBranchesException;
import com.saltaTech.branch.domain.dto.request.BranchCreateRequest;
import com.saltaTech.branch.domain.dto.request.BranchSearchCriteria;
import com.saltaTech.branch.domain.dto.request.BranchUpdateRequest;
import com.saltaTech.branch.domain.dto.response.BranchResponse;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Interfaz que define las operaciones disponibles para gestionar
 * las sucursales (branches) de una organización.
 *
 * <p>Incluye métodos para crear, actualizar, eliminar y consultar
 * sucursales, así como para listar todas las sucursales con
 * soporte de paginación y criterios de búsqueda.</p>
 */
public interface BranchService{
	/**
	 * Obtiene todas las sucursales que cumplen con los criterios de búsqueda proporcionados.
	 *
	 * @param pageable Parámetros de paginación como página, tamaño y ordenamiento.
	 * @param searchCriteria Criterios de búsqueda para filtrar sucursales.
	 * @return Página de {@link BranchResponse} con las sucursales encontradas.
	 * @throws NoBranchesException si no se encuentra ninguna sucursal.
	 */
	Page<BranchResponse> findAll(Pageable pageable, BranchSearchCriteria searchCriteria);

	/**
	 * Busca una sucursal por su identificador único.
	 *
	 * @param id Identificador de la sucursal.
	 * @return La sucursal encontrada en formato {@link BranchResponse}.
	 * @throws BranchNotFoundException si no existe ninguna sucursal con el ID dado.
	 */
	BranchResponse findById(Long id);

	/**
	 * Crea una nueva sucursal asociada a la organización del contexto actual.
	 *
	 * @param createRequest DTO con la información necesaria para crear la sucursal.
	 * @return La sucursal creada en formato {@link BranchResponse}.
	 * @throws OrganizationNotFoundException si no se encuentra la organización actual.
	 */
	BranchResponse create(BranchCreateRequest createRequest);

	/**
	 * Actualiza los datos de una sucursal existente.
	 *
	 * @param updateRequest DTO con la información a actualizar.
	 * @param id Identificador de la sucursal a actualizar.
	 * @return La sucursal actualizada en formato {@link BranchResponse}.
	 * @throws BranchNotFoundException si no se encuentra la sucursal con el ID dado.
	 */
	BranchResponse update(BranchUpdateRequest updateRequest, Long id);

	/**
	 * Elimina una sucursal por su identificador.
	 *
	 * @param id Identificador de la sucursal a eliminar.
	 * @throws BranchNotFoundException si no se encuentra la sucursal con el ID dado.
	 */
	void deleteById(Long id);

}
