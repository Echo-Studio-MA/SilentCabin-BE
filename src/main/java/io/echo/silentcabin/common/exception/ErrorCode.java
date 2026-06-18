package io.echo.silentcabin.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /*User*/
    EXIST_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),
    /*Common*/
    CABIN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 에러입니다.");
    private final HttpStatus status;
    private final String message;
}
