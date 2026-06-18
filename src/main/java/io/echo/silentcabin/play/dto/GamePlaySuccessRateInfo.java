package io.echo.silentcabin.play.dto;

public record GamePlaySuccessRateInfo(
        Long successCount,
        Long failCount
) {
}
