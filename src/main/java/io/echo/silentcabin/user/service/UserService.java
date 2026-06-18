package io.echo.silentcabin.user.service;

import io.echo.silentcabin.common.exception.CabinException;
import io.echo.silentcabin.common.exception.ErrorCode;
import io.echo.silentcabin.user.domain.RefreshToken;
import io.echo.silentcabin.user.domain.User;
import io.echo.silentcabin.user.dto.*;
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
import java.util.List;

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

    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {

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
        return new LoginResponseDto(user.getNickname(), keyPair.accessToken(), keyPair.refreshToken(), keyPair.accessExpiresInSeconds());
    }

    // 토큰 갱신
    @Transactional
    public KeyPair refresh(RefreshRequestDto request) {
        //리프레쉬 토큰 확인
        RefreshToken refreshToken = refreshRepository.findByRefreshToken(request.refreshToken())
                .orElseThrow(() -> new CabinException(ErrorCode.INVALID_REFRESH_TOKEN));

        // 유효시간이 지났으면 유호하지 않은 토큰
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new CabinException(ErrorCode.INVALID_REFRESH_TOKEN);

        // 유효하다면 AccessToken/RefreshToken 다시 생성
        User user = refreshToken.getUser();
        KeyPair keyPair = tokenProvider.issueKeyPair(user.getId(), user.getEmail(), user.getRole());

        //refresh토큰 유효시간 추출
        Date expiration = tokenProvider.parseExpiration(keyPair.refreshToken());
        LocalDateTime expirationTime =
                expiration.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

        refreshToken.update(keyPair.refreshToken(), expirationTime);

        // 반환
        return keyPair;
    }

    // 로그아웃
    @Transactional
    public void logout(AuthUser authUser) {
        // 유저 찾기
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new CabinException(ErrorCode.USER_NOT_FOUND));

        // 해당 유저의 refresh 토큰 모두 삭제
        List<RefreshToken> refreshTokens = refreshRepository.findByUserId(user.getId());
        refreshRepository.deleteAll(refreshTokens);
    }

    @Transactional(readOnly = true)
    public User getUserFromAuthUser(AuthUser authUser) {
        return userRepository.findById(authUser.getId()).orElseThrow(()->new CabinException(ErrorCode.USER_NOT_FOUND));
    }
}
