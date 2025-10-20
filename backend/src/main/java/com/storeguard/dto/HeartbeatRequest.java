package com.storeguard.dto;

import jakarta.validation.constraints.NotBlank;

public record HeartbeatRequest(
        @NotBlank String sessionId,
        @NotBlank String cameraId
) {}
