package io.echo.silentcabin.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record SignUpRequestDto(
        @Email(message = "이메일 형식으로 입력해주세요")
        String email,

        @NotEmpty(message = "닉네임을 입력해주세요.")
        @Length(min = 2, max = 20)
        String nickname,

        @NotNull
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,20}$",
                message = "비밀번호는 8~20자의 영문, 숫자, 특수문자를 포함해야 합니다."
        )
        String password
) {
}
