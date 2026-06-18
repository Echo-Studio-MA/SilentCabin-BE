package io.echo.silentcabin.play.controller;

import io.echo.silentcabin.common.dto.ApiResponse;
import io.echo.silentcabin.common.success.SuccessCode;
import io.echo.silentcabin.play.dto.GamePlayRankResponseDto;
import io.echo.silentcabin.play.dto.GamePlaySaveRequestDto;
import io.echo.silentcabin.play.service.GamePlayService;
import io.echo.silentcabin.user.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plays")
public class GamePlayController {
    private final GamePlayService  gamePlayService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveGamePlay(@AuthenticationPrincipal AuthUser authUser,
                                                          @RequestBody @Valid GamePlaySaveRequestDto request) {
        gamePlayService.save(authUser, request);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_SAVE_PLAY.getMessage()));

    }

    @GetMapping("/ranking")
    public ResponseEntity<ApiResponse<List<GamePlayRankResponseDto>>> getRank(@AuthenticationPrincipal AuthUser authUser) {
        List<GamePlayRankResponseDto> response = gamePlayService.getRank(authUser);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_FIND_RANKING.getMessage(), response));
    }
}
