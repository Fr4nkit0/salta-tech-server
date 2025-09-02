package com.saltaTech.organization.application.service.interfaces;

import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import com.saltaTech.organization.domain.dto.request.OrganizationCreateRequest;
import com.saltaTech.organization.domain.dto.request.OrganizationSearchCriteria;
import com.saltaTech.organization.domain.dto.request.OrganizationUpdateRequest;
import com.saltaTech.organization.domain.dto.response.OrganizationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Interfaz que define las operaciones disponibles para gestionar
 * organizaciones y sus sucursales asociadas.
 *
 * <p>Incluye métodos para crear, actualizar, eliminar y consultar
 * organizaciones, así como para listar todas las organizaciones con
 * soporte de paginación y criterios de búsqueda.</p>
 */
public interface OrganizationService {

	/**
	 * Obtiene todas las organizaciones que cumplen con los criterios de búsqueda proporcionados.
	 *
	 * @param pageable Parámetros de paginación como página, tamaño y ordenamiento.
	 * @param searchCriteria Criterios de búsqueda para filtrar organizaciones.
	 * @return Página de {@link OrganizationResponse} con las organizaciones encontradas.
	 * @throws RuntimeException si no se encuentra ninguna organización.
	 */
	Page<OrganizationResponse> findAll(Pageable pageable, OrganizationSearchCriteria searchCriteria);

	/**
	 * Busca una organización por su identificador único.
	 *
	 * @param id Identificador de la organización.
	 * @return La organización encontrada en formato {@link OrganizationResponse}.
	 * @throws OrganizationNotFoundException si no existe ninguna organización con el ID dado.
	 */
	OrganizationResponse findById(Long id);

	/**
	 * Busca una organización por su slug único.
	 *
	 * @param slug Slug de la organización.
	 * @return La organización encontrada en formato {@link OrganizationResponse}.
	 * @throws OrganizationNotFoundException si no se encuentra la organización con el slug dado.
	 */
	OrganizationResponse findOrganizationBySlug(String slug);

	/**
	 * Crea una nueva organización y su sucursal principal asociada.
	 *
	 * @param createRequest DTO con la información necesaria para crear la organización.
	 * @return La organización creada en formato {@link OrganizationResponse}.
	 */
	OrganizationResponse create(OrganizationCreateRequest createRequest);

	/**
	 * Actualiza los datos de una organización existente.
	 *
	 * @param updateRequest DTO con la información a actualizar.
	 * @param id Identificador de la organización a actualizar.
	 * @return La organización actualizada en formato {@link OrganizationResponse}.
	 * @throws OrganizationNotFoundException si no se encuentra la organización con el ID dado.
	 */
	OrganizationResponse update(OrganizationUpdateRequest updateRequest, Long id);

	/**
	 * Elimina una organización de manera lógica usando su slug.
	 *
	 * @param slug Slug de la organización a eliminar.
	 */
	void deleteOrganization(String slug);

	/**
	 * Elimina una organización de manera física usando su ID.
	 *
	 * @param id Identificador de la organización a eliminar.
	 */
	void deleteById(Long id);

}
