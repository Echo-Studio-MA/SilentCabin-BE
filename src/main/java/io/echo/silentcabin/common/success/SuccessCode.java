package io.echo.silentcabin.common.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    /*User*/
    SUCCESS_SIGNUP("계정이 정상적으로 생성되었습니다."),
    SUCCESS_LOGIN("정상적으로 로그인되었습니다."),
    SUCCESS_REFRESH_TOKEN("정상적으로 토큰이 갱신되었습니다."),
    SUCCESS_LOGOUT("정상적으로 로그아웃되었습니다."),

    /*Play*/
    SUCCESS_SAVE_PLAY("정상적으로 플레이 기록이 저장되었습니다."),
    SUCCESS_FIND_RANKING("정상적으로 랭킹이 조회되었습니다."),
    /*Common*/
    CABIN_SUCCESS("요청이 정상적으로 처리되었습니다.");


    private final String message;

}
