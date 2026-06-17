package io.echo.silentcabin.user.controller;

import io.echo.silentcabin.common.dto.ApiResponse;
import io.echo.silentcabin.common.success.SuccessCode;
import io.echo.silentcabin.user.dto.KeyPair;
import io.echo.silentcabin.user.dto.LoginRequestDto;
import io.echo.silentcabin.user.dto.SignUpRequestDto;
import io.echo.silentcabin.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody SignUpRequestDto request) {
        userService.signUp(request);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.SUCCESS_SIGNUP.getMessage()), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<KeyPair>> login(@RequestBody LoginRequestDto request) {
        KeyPair keyPair = userService.login(request);
        return new ResponseEntity<>(ApiResponse.success(SuccessCode.SUCCESS_LOGIN.getMessage(), keyPair), HttpStatus.OK);
    }
}
