package io.echo.silentcabin.common.exception;

import lombok.Getter;

@Getter
public class CabinException extends RuntimeException {
    private final ErrorCode errorCode;

    public CabinException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public CabinException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage()+" "+message);
        this.errorCode = errorCode;
    }

}
