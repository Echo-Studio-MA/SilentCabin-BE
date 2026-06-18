package io.echo.silentcabin.play.dto;

public record GamePlayRankResponseDto(
        Long id,
        Integer rank,
        Long userId,
        String nickname,
        Integer score
) {
}
