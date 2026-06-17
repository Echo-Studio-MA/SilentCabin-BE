package io.echo.silentcabin.user.dto;


import io.echo.silentcabin.user.domain.Role;

public record TokenBody(
        Long userId,
        String email,
        Role role
) {
}
