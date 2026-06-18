package io.echo.silentcabin.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record RefreshRequestDto(
        @NotEmpty
        String refreshToken
) {
}
