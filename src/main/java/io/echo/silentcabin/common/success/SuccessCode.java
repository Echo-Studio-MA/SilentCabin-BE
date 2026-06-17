package io.echo.silentcabin.common.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    SUCCESS_SIGNUP("계정이 정상적으로 생성되었습니다."),
    CABIN_SUCCESS("요청이 정상적으로 처리되었습니다.");


    private final String message;

}
