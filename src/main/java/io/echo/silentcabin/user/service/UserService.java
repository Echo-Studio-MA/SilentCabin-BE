package io.echo.silentcabin.user.service;

import io.echo.silentcabin.common.exception.CabinException;
import io.echo.silentcabin.common.exception.ErrorCode;
import io.echo.silentcabin.user.domain.RefreshToken;
import io.echo.silentcabin.user.domain.User;
import io.echo.silentcabin.user.dto.AuthUser;
import io.echo.silentcabin.user.dto.KeyPair;
import io.echo.silentcabin.user.dto.LoginRequestDto;
import io.echo.silentcabin.user.dto.SignUpRequestDto;
import io.echo.silentcabin.user.repository.RefreshTokenRepository;
import io.echo.silentcabin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshRepository;

    //회원가입
    @Transactional
    public void signUp(SignUpRequestDto request) {
        //이메일 중복 확인
        if (userRepository.existsByEmail(request.email())) throw new CabinException(ErrorCode.EXIST_EMAIL);

        //회원 저장
        User user = new User(request.email(), request.nickname(), passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CabinException(ErrorCode.USER_NOT_FOUND));
        return new AuthUser(user.getId(), user.getEmail(), user.getPassword(), user.getRole().name());
    }

    public KeyPair login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new CabinException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) throw new CabinException(ErrorCode.PASSWORD_NOT_MATCH);

        user.updateLastLoginAt();

        // 토큰 발급
        KeyPair keyPair = tokenProvider.issueKeyPair(user.getId(), user.getEmail(), user.getRole());

        //refresh토큰 유효시간 추출
        Date expiration = tokenProvider.parseExpiration(keyPair.refreshToken());
        LocalDateTime expirationTime =
                expiration.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

        // refresh토큰 db 저장
        refreshRepository.save(new RefreshToken(keyPair.refreshToken(),expirationTime, user));
        return keyPair;
    }
}
