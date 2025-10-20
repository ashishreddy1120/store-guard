package com.storeguard.dto;

import jakarta.validation.constraints.NotBlank;

public record EndSessionRequest(@NotBlank String sessionId) {}
