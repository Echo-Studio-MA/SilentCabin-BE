package io.echo.silentcabin.user.dto;

public record LoginResponseDto(
        String nickname,
        String accessToken,
        String refreshToken,
        Long accessExpiresInSeconds
) {
}
