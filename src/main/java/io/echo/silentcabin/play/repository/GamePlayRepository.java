package io.echo.silentcabin.play.repository;

import io.echo.silentcabin.play.domain.GamePlay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayRepository extends JpaRepository<GamePlay, Long> {
}
