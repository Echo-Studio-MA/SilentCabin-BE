package io.echo.silentcabin.play.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record GamePlaySaveRequestDto(
        @Min(value = 0, message = "점수는 0 이상이어야 합니다.")
        @Max(value = 10000, message = "점수는 10000 이하이어야 합니다.")
        Integer score,
        @NotNull(message = "성공 여부는 필수값입니다.")
        Boolean isClear,
        @NotNull(message = "시작시간은 필수값입니다.")
        LocalDateTime startedAt,
        @NotNull(message = "종료시간은 필수값입니다.")
        LocalDateTime endedAt
) {
}
