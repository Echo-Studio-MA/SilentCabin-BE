package io.echo.silentcabin.play.service;

import io.echo.silentcabin.play.domain.GamePlay;
import io.echo.silentcabin.play.dto.GamePlayRankResponseDto;
import io.echo.silentcabin.play.dto.GamePlaySaveRequestDto;
import io.echo.silentcabin.play.repository.GamePlayRepository;
import io.echo.silentcabin.user.domain.User;
import io.echo.silentcabin.user.dto.AuthUser;
import io.echo.silentcabin.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional(readOnly = true)
    public List<GamePlayRankResponseDto> getRank(AuthUser authUser) {
        List<GamePlayRankResponseDto> response = new ArrayList<>();
        List<GamePlay> rank = gamePlayRepository.findTop10ByOrderByScoreDesc();
        for (int i = 0; i < rank.size(); i++) {
            GamePlay gamePlay = rank.get(i);
            response.add(new GamePlayRankResponseDto(gamePlay.getId(), i+1, gamePlay.getUser().getId(), gamePlay.getUser().getNickname(), gamePlay.getScore()));
        }
        return response;
    }
}
