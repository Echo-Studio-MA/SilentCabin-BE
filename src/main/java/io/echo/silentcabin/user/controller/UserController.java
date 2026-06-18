package io.echo.silentcabin.user.controller;

import io.echo.silentcabin.common.dto.ApiResponse;
import io.echo.silentcabin.common.success.SuccessCode;
import io.echo.silentcabin.user.dto.*;
import io.echo.silentcabin.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody SignUpRequestDto request) {
        userService.signUp(request);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.SUCCESS_SIGNUP.getMessage()), HttpStatus.OK);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<KeyPair>> login(@RequestBody LoginRequestDto request) {
        KeyPair keyPair = userService.login(request);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.SUCCESS_LOGIN.getMessage(), keyPair), HttpStatus.OK);
    }

    //토큰 갱신
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<KeyPair>> refresh(@RequestBody @Valid RefreshRequestDto request) {
        KeyPair keypair = userService.refresh(request);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.SUCCESS_REFRESH_TOKEN.getMessage(),keypair), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal AuthUser user) {
        userService.logout(user);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.SUCCESS_LOGOUT.getMessage()), HttpStatus.OK);
    }
}
