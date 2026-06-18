package io.echo.silentcabin.common.config;

import io.echo.silentcabin.play.domain.GamePlay;
import io.echo.silentcabin.play.dto.GamePlaySaveRequestDto;
import io.echo.silentcabin.play.repository.GamePlayRepository;
import io.echo.silentcabin.user.domain.User;
import io.echo.silentcabin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitConfig {
    private final UserRepository userRepository;
    private final GamePlayRepository gamePlayRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    CommandLineRunner init() {
        return _ -> {
            if (userRepository.count() > 0) return;

            User user = new User("test@example.com", "test", passwordEncoder.encode("test1234!"));
            userRepository.save(user);

            LocalDateTime base = LocalDateTime.now().minusDays(7);

            List<GamePlaySaveRequestDto> plays = List.of(
                    play(9100, true,  base.plusDays(0).withHour(10), 390),
                    play(8500, true,  base.plusDays(0).withHour(14), 480),
                    play(3400, false, base.plusDays(1).withHour(11), 900),
                    play(7200, true,  base.plusDays(1).withHour(20), 620),
                    play(9800, true,  base.plusDays(2).withHour(9),  340),
                    play(2100, false, base.plusDays(3).withHour(13), 300),
                    play(6800, true,  base.plusDays(3).withHour(18), 510),
                    play(8200, true,  base.plusDays(4).withHour(10), 450),
                    play(5500, false, base.plusDays(5).withHour(15), 1200),
                    play(7700, true,  base.plusDays(6).withHour(21), 580)
            );

            plays.stream()
                    .map(dto -> new GamePlay(dto, user))
                    .forEach(gamePlayRepository::save);
        };
    }

    private GamePlaySaveRequestDto play(int score, boolean isClear, LocalDateTime start, int durationSeconds) {
        return new GamePlaySaveRequestDto(score, isClear, start, start.plusSeconds(durationSeconds));
    }
}
