package io.echo.silentcabin.user.repository;

import io.echo.silentcabin.user.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    List<RefreshToken> findByUserId(Long userId);
}
