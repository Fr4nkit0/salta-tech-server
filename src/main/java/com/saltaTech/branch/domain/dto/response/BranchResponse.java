package com.saltaTech.branch.domain.dto.response;



import java.io.Serializable;

public record BranchResponse(
		Long id,
		String organizationName,
		String name
) implements Serializable {
}
