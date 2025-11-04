package com.saltaTech.branch.application.service.interfaces;

import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.branch.domain.dto.request.BranchCreateRequest;
import com.saltaTech.branch.domain.dto.request.BranchSearchCriteria;
import com.saltaTech.branch.domain.dto.request.BranchUpdateRequest;
import com.saltaTech.branch.domain.dto.response.BranchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Interfaz que define las operaciones disponibles para gestionar
 * sucursales.
 * <p>Incluye métodos para crear, actualizar, eliminar y consultar
 * Sucursales, así como para listar todas las organizaciones con
 * soporte de paginación y criterios de búsqueda.</p>
 */
public interface BranchService {

	/**
	 * Obtiene todas las Sucursales que cumplen con los criterios de búsqueda proporcionados.
	 *
	 * @param pageable Parámetros de paginación como página, tamaño y ordenamiento.
	 * @param searchCriteria Criterios de búsqueda para filtrar organizaciones.
	 * @return Página de {@link BranchResponse} con las organizaciones encontradas.
	 * @throws RuntimeException si no se encuentra ninguna organización.
	 */
	Page<BranchResponse> findAll(Pageable pageable, BranchSearchCriteria searchCriteria);

	/**
	 * Busca una Sucursal por su identificador único.
	 *
	 * @param id Identificador de la organización.
	 * @return La Sucursal encontrada en formato {@link BranchResponse}.
	 * @throws BranchNotFoundException si no existe ninguna Sucursal con el ID dado.
	 */
	BranchResponse findById(Long id);

	/**
	 * Busca una Sucursal por un indentificador único.
	 *
	 * @param identifier Slug de la organización.
	 * @return La organización encontrada en formato {@link BranchResponse}.
	 * @throws BranchNotFoundException si no se encuentra la organización con el slug dado.
	 */
	BranchResponse findOrganizationByIdentifier(String identifier);

	/**
	 * Crea una nueva Sucursal.
	 *
	 * @param createRequest DTO con la información necesaria para crear la Sucursal.
	 * @return La organización creada en formato {@link BranchResponse}.
	 */
	BranchResponse create(BranchCreateRequest createRequest);

	/**
	 * Actualiza los datos de una Sucursal existente.
	 *
	 * @param updateRequest DTO con la información a actualizar.
	 * @param id Identificador de la sucursal a actualizar.
	 * @return La Sucursal actualizada en formato {@link BranchResponse}.
	 * @throws BranchNotFoundException si no se encuentra la organización con el ID dado.
	 */
	BranchResponse update(BranchUpdateRequest updateRequest, Long id);

	/**
	 * Elimina una Sucursal de manera lógica usando su indentificador.
	 *
	 * @param identifier Indentificador de la Sucursal a eliminar.
	 */
	void deleteOrganizationByIdentifier(String identifier);

	/**
	 * Elimina una Sucursal de manera física usando su ID.
	 *
	 * @param id Identificador de la Sucursal a eliminar.
	 */
	void deleteById(Long id);

}
