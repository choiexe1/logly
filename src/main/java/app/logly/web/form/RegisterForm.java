package app.logly.web.form;

import jakarta.validation.constraints.NotBlank;

public record RegisterForm(
        @NotBlank(message = "유저네임은 필수 입력 값입니다.") String username,
        @NotBlank(message = "닉네임은 필수 입력 값입니다.") String nickname,
        @NotBlank(message = "이메일은 필수 입력 값입니다.") String email,
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.") String password,
        @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.") String confirmPassword,
        Boolean subscribeNewsletter

) {
}
