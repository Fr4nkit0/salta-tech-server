package com.saltaTech.common.application.exceptions.custom;

import java.lang.reflect.Field;

public abstract class NoDataFoundException extends RuntimeException {
	protected final Object criteria;

	protected NoDataFoundException(Object criteria, Throwable cause) {
		super(cause);
		this.criteria = criteria;
	}

	protected NoDataFoundException(Object criteria) {
		this.criteria = criteria;
	}

	protected NoDataFoundException() {
		this.criteria = null;
	}

	public abstract String getDataType();

	@Override
	public String getMessage() {
		StringBuilder message = new StringBuilder();
		message.append("No se encontraron ").append(getDataType());

		if (criteria != null) {
			String formattedCriteria = formatCriteria(criteria);
			if (!formattedCriteria.isEmpty()) {
				message.append(" para los criterios: ").append(formattedCriteria);
			}
		}

		message.append(".");

		if (getCause() != null) {
			message.append(" Causa: ").append(getCause().getMessage());
		}

		return message.toString();
	}


	private String formatCriteria(Object obj) {
		StringBuilder sb = new StringBuilder();
		try {
			Class<?> clazz = obj.getClass();
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(obj);

				if (value != null && !value.toString().trim().isEmpty()) {
					sb.append(field.getName())
							.append("=")
							.append(value)
							.append(", ");
				}
			}
		} catch (Exception e) {
			return obj.toString(); // fallback si falla la reflexión
		}

		if (sb.length() > 2) {
			sb.setLength(sb.length() - 2); // eliminar la última coma y espacio
		}

		return sb.toString();
	}

	public Object getCriteria() {
		return criteria;
	}
}
