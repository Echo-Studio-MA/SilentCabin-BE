package io.echo.silentcabin.play.domain;

import io.echo.silentcabin.common.domain.BaseEntity;
import io.echo.silentcabin.play.dto.GamePlaySaveRequestDto;
import io.echo.silentcabin.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "game_plays")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GamePlay extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Integer score;  //점수

    @Column(nullable = false)
    private Long clearTime;   //걸린 시간(초)

    @Column(nullable = false)
    private Boolean isClear;  //클리어 여부

    @Column(nullable = false)
    private LocalDateTime startedAt;  //시작 시간

    @Column(nullable = false)
    private LocalDateTime endedAt;  //끝난 시간


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public GamePlay(GamePlaySaveRequestDto request, User user) {
        this.score = request.score();
        this.isClear = request.isClear();
        this.startedAt = request.startedAt();
        this.endedAt = request.endedAt();
        this.clearTime = Duration.between(startedAt, endedAt).getSeconds();
        this.user = user;
    }
}
