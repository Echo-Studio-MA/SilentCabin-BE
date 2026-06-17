package io.echo.silentcabin.common.config;

import io.echo.silentcabin.user.domain.User;
import io.echo.silentcabin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class DataInitConfig {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() < 1) {
                User user = new User("test@example.com", "test",passwordEncoder.encode("test1234!"));
                userRepository.save(user);
            }
        };
    }


}
