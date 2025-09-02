package com.saltaTech.branch.domain.dto.request;

import java.io.Serializable;

public record BranchUpdateRequest(
		String name
) implements Serializable {
}
