package io.echo.silentcabin.play.dto;

public record GamePlaySuccessRateResponseDto(
        Long successCount,
        Long failCount
) {
}
