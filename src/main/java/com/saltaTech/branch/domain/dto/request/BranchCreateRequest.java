package com.saltaTech.branch.domain.dto.request;



import java.io.Serializable;

public record BranchCreateRequest(
		String name
) implements Serializable {
}
