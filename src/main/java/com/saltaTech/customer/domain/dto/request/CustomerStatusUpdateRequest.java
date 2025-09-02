package com.saltaTech.customer.domain.dto.request;

import com.saltaTech.customer.domain.util.Status;
import jakarta.validation.constraints.NotNull;

public record CustomerStatusUpdateRequest(
        @NotNull Status status
) {
}
