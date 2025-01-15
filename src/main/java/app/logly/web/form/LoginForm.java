package app.logly.web.form;

import jakarta.validation.constraints.NotBlank;

public record LoginForm(
        @NotBlank(message = "유저네임은 필수 입력 값입니다.") String username,
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.") String password
) {
}
