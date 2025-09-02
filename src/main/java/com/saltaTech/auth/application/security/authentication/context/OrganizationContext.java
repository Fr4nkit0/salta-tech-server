package com.saltaTech.auth.application.security.authentication.context;
/**
 * Contexto de la organización  para la solicitud actual.
 * <p>
 * Esta clase utilitaria utiliza variables {@link ThreadLocal} para almacenar y
 * proveer el Slug de la organización de forma aislada por hilo,
 * garantizando que la información sea accesible durante todo el procesamiento
 * de la petición sin interferencias entre hilos.
 * @author Franco
 */
public class OrganizationContext {
	private static final ThreadLocal<String> currentOrganization = new ThreadLocal<>();

	private OrganizationContext() {
		throw new IllegalStateException("Utility class");
	}
	/**
	 * Establece el contexto actual de organización para el hilo en ejecución.
	 *
	 * @param organizationSlug Slug de la organización.
	 */
	public static void set(String organizationSlug) {
		currentOrganization.set(organizationSlug);
	}
	/**
	 * Obtiene el Slug de la organización almacenado en el contexto del hilo actual.
	 * @return Slug de la organización, o {@code null} si no se ha establecido.
	 */
	public static String getOrganizationSlug() {
		return currentOrganization.get();
	}

	/**
	 * Limpia el contexto eliminando la información almacenada para organización.
	 * Esto es importante para evitar fugas de información entre peticiones en un entorno multihilo.
	 */
	public static void clear() {
		currentOrganization.remove();
	}
}
