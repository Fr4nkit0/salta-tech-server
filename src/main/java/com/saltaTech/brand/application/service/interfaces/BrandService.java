package com.saltaTech.brand.application.service.interfaces;

import com.saltaTech.brand.application.exceptions.BrandNotFoundException;
import com.saltaTech.brand.application.exceptions.NoBrandsFoundException;
import com.saltaTech.brand.domain.dto.request.BrandCreateRequest;
import com.saltaTech.brand.domain.dto.request.BrandSearchCriteria;
import com.saltaTech.brand.domain.dto.request.BrandUpdateRequest;
import com.saltaTech.brand.domain.dto.response.BrandResponse;
import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio para gestionar marcas de productos dentro de la organización.
 * Todos los métodos aseguran que las operaciones respeten la organización
 * actual según el contexto de ejecución.
 */
public interface BrandService {
	/**
	 * Obtiene una página de marcas según los criterios de búsqueda proporcionados.
	 *
	 * @param pageable       Información de paginación (página, tamaño, ordenamiento).
	 * @param searchCriteria Criterios de filtrado de marcas.
	 * @return Una página de {@link BrandResponse} que cumple con los criterios.
	 * @throws NoBrandsFoundException Si no se encuentra ninguna marca con los criterios dados.
	 */
	Page<BrandResponse> findAll(Pageable pageable, BrandSearchCriteria searchCriteria);

	/**
	 * Busca una marca por su ID.
	 *
	 * @param id ID de la marca.
	 * @return La marca encontrada como {@link BrandResponse}.
	 * @throws BrandNotFoundException Si no se encuentra una marca con el ID dado.
	 */
	BrandResponse findById(Long id);

	/**
	 * Crea una nueva marca dentro de la organización actual.
	 *
	 * @param createRequest Datos necesarios para crear la marca.
	 * @return La marca creada como {@link BrandResponse}.
	 * @throws BranchNotFoundException Si no se encuentra la organización actual.
	 */
	BrandResponse create(BrandCreateRequest createRequest);

	/**
	 * Actualiza una marca existente.
	 *
	 * @param updateRequest Datos a actualizar de la marca.
	 * @param id            ID de la marca a actualizar.
	 * @return La marca actualizada como {@link BrandResponse}.
	 * @throws BrandNotFoundException Si no se encuentra una marca con el ID dado.
	 */
	BrandResponse update(BrandUpdateRequest updateRequest, Long id);

	/**
	 * Elimina una marca por su ID.
	 *
	 * @param id ID de la marca a eliminar.
	 * @throws BrandNotFoundException Si no se encuentra una marca con el ID dado.
	 */
	void deleteById(Long id);
}