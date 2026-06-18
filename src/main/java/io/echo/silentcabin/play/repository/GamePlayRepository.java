package io.echo.silentcabin.play.repository;

import io.echo.silentcabin.play.domain.GamePlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GamePlayRepository extends JpaRepository<GamePlay, Long> {
    @Query("""
        select g
        from GamePlay g
        join fetch g.user
        order by g.score desc
        limit 10
        """)
    List<GamePlay> findTop10ByOrderByScoreDesc();

}
