package io.echo.silentcabin.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /*User*/
    EXIST_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이미 존재하는 회원입니다."),
    /*Common*/
    CABIN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 에러입니다.");
    private final HttpStatus status;
    private final String message;
}
