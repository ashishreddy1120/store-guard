package com.storeguard.dto;

import jakarta.validation.constraints.*;
import java.time.Instant;

public record ItemInteractionRequest(
        @NotBlank String sessionId,
        @NotBlank String cameraId,
        @NotNull Instant occurredAt,
        @NotBlank String action,
        String productId,
        @Min(1) int quantity,
        @DecimalMin("0.0") @DecimalMax("1.0") double confidence
) {}
