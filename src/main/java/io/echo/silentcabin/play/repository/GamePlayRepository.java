package io.echo.silentcabin.play.repository;

import io.echo.silentcabin.play.domain.GamePlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GamePlayRepository extends JpaRepository<GamePlay, Long> {
    @Query("""
        select g
        from GamePlay g
        where g.score = (
            select max(g2.score)
            from GamePlay g2
            where g2.user = g.user
        )
        order by g.score desc
        """)
    List<GamePlay> findTop10ByOrderByScoreDesc();

}
