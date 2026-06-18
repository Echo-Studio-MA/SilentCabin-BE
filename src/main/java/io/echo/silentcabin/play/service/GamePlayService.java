package io.echo.silentcabin.play.service;

import io.echo.silentcabin.play.domain.GamePlay;
import io.echo.silentcabin.play.dto.GamePlaySaveRequestDto;
import io.echo.silentcabin.play.repository.GamePlayRepository;
import io.echo.silentcabin.user.domain.User;
import io.echo.silentcabin.user.dto.AuthUser;
import io.echo.silentcabin.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GamePlayService {
    private final GamePlayRepository gamePlayRepository;
    private final UserService userService;

    //플레이 기록 저장
    @Transactional
    public void save(AuthUser authUser, GamePlaySaveRequestDto request) {
        User user = userService.getUserFromAuthUser(authUser);
        GamePlay gamePlay = new GamePlay(request, user);
        gamePlayRepository.save(gamePlay);
    }
}
