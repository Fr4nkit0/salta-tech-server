package com.saltaTech.common.application.exceptions.custom;

import lombok.Getter;

import java.io.Serializable;

public abstract class ResourceNotFoundException extends RuntimeException {
	@Getter
	protected final Serializable resourceId;
	protected final String resourceName;

	protected ResourceNotFoundException(Serializable resourceId, Throwable cause) {
		super(cause);
		this.resourceId = resourceId;
		this.resourceName = getResourceName();
	}

	protected ResourceNotFoundException(Serializable resourceId) {
		this.resourceId = resourceId;
		this.resourceName = getResourceName();
	}

	public abstract String getResourceName();

	@Override
	public String getMessage() {
		StringBuilder message = new StringBuilder();

		message.append(String.format("%s con indentificador: ['%s'] no encontrado",
				resourceName, resourceId));

		if (getCause() != null) {
			message.append(" | Cause: ").append(getCause().getMessage());
		}

		return message.toString();
	}

}

