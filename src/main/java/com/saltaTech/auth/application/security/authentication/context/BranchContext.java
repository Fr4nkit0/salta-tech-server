package com.saltaTech.auth.application.security.authentication.context;
/**
 * Contexto de la sucursal para la solicitud actual.
 * <p>
 * Esta clase utilitaria utiliza variables {@link ThreadLocal} para almacenar y
 * proveer la indentificacion de la sucursal de forma aislada por hilo,
 * garantizando que la información sea accesible durante todo el procesamiento
 * de la petición sin interferencias entre hilos.
 * @author Franco
 */
public class BranchContext {
	private static final ThreadLocal<String> currentBranch = new ThreadLocal<>();

	private BranchContext() {
		throw new IllegalStateException("Utility class");
	}
	/**
	 * Establece el contexto actual de la Sucursal para el hilo en ejecución.
	 * @param tenant el indentificador de la Sucursal.
	 */
	public static void set(String tenant) {
		currentBranch.set(tenant);
	}
	/**
	 * Obtiene el indentificador de la Sucursal almacenado en el contexto del hilo actual.
	 * @return Indentificador de la Sucursal, o {@code null} si no se ha establecido.
	 */
	public static String getBranchTenant() {
		return currentBranch.get();
	}

	/**
	 * Limpia el contexto eliminando el indentificador de la organizacion.
	 * Esto es importante para evitar fugas de información entre peticiones en un entorno multihilo.
	 */
	public static void clear() {
		currentBranch.remove();
	}
}
