package io.echo.silentcabin.play.repository;

import io.echo.silentcabin.play.domain.GamePlay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamePlayRepository extends JpaRepository<GamePlay, Long> {
    List<GamePlay> findAllByUserId(Long userId);
}
