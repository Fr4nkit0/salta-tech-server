package com.saltaTech.category.application.services.interfaces;

import com.saltaTech.category.application.exceptions.CategoryNotFoundException;
import com.saltaTech.category.application.exceptions.NoCategoriesFoundException;
import com.saltaTech.category.domain.dto.request.CategoryCreateRequest;
import com.saltaTech.category.domain.dto.request.CategorySearchCriteria;
import com.saltaTech.category.domain.dto.request.CategoryUpdateRequest;
import com.saltaTech.category.domain.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio para gestionar categorías de productos dentro de la organización.
 * Todos los métodos aseguran que las operaciones respeten la organización
 * actual según el contexto de ejecución.
 */
public interface CategoryService {
	/**
	 * Obtiene una página de categorías según los criterios de búsqueda proporcionados.
	 *
	 * @param pageable        Información de paginación (página, tamaño, ordenamiento).
	 * @param searchCriteria  Criterios de filtrado de categorías.
	 * @return Una página de {@link CategoryResponse} que cumple con los criterios.
	 * @throws NoCategoriesFoundException Si no se encuentra ninguna categoría con los criterios dados.
	 */
	Page<CategoryResponse> findAll(Pageable pageable, CategorySearchCriteria searchCriteria);

	/**
	 * Busca una categoría por su ID.
	 *
	 * @param id  ID de la categoría.
	 * @return La categoría encontrada como {@link CategoryResponse}.
	 * @throws CategoryNotFoundException Si no se encuentra una categoría con el ID dado.
	 */
	CategoryResponse findById(Long id);

	/**
	 * Crea una nueva categoría dentro de la organización actual.
	 *
	 * @param createRequest Datos necesarios para crear la categoría.
	 * @return La categoría creada como {@link CategoryResponse}.
	 */
	CategoryResponse create(CategoryCreateRequest createRequest);

	/**
	 * Actualiza una categoría existente.
	 *
	 * @param request Datos a actualizar de la categoría.
	 * @param id      ID de la categoría a actualizar.
	 * @return La categoría actualizada como {@link CategoryResponse}.
	 * @throws CategoryNotFoundException Si no se encuentra una categoría con el ID dado.
	 */
	CategoryResponse update(CategoryUpdateRequest request, Long id);

	/**
	 * Elimina una categoría por su ID.
	 *
	 * @param id ID de la categoría a eliminar.
	 * @throws CategoryNotFoundException Si no se encuentra una categoría con el ID dado.
	 */
	void deleteById(Long id);
}
