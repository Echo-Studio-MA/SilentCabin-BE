package io.echo.silentcabin.user.dto;

public record KeyPair(
        String accessToken,
        String refreshToken,
        Long accessExpiresInSeconds
) {

}
