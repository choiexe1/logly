package app.logly.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record RegisterForm(
        @NotBlank(message = "유저네임은 필수 입력 값입니다.")
        @Length(min = 5, max = 12, message = "유저네임은 최소 5 이상, 12 이하의 길이를 가져야합니다.")
        String username,

        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        @Length(min = 3, max = 10, message = "닉네임은 최소 3 이상, 최대 10 이하의 길이를 가져야합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣_]+$", message = "닉네임은 영문, 한글, 숫자, 언더바(_)만 사용할 수 있습니다.")
        String nickname,

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "유효한 이메일 주소를 입력해주세요.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Length(min = 4, max = 20, message = "비밀번호는 최소 4 이상, 20 이하의 길이를 가져야합니다.")
        String password,

        @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.") String confirmPassword,
        Boolean subscribeNewsletter
) {
}
