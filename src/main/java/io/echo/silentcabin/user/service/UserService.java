package io.echo.silentcabin.user.service;

import io.echo.silentcabin.common.exception.CabinException;
import io.echo.silentcabin.common.exception.ErrorCode;
import io.echo.silentcabin.user.domain.User;
import io.echo.silentcabin.user.dto.AuthUser;
import io.echo.silentcabin.user.dto.SignUpRequestDto;
import io.echo.silentcabin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CabinException(ErrorCode.USER_NOT_FOUND));
        return new AuthUser(user.getId(), user.getEmail(), user.getPassword(), user.getRole().name());
    }
}
