package app.logly.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record LoginForm(
        @NotBlank(message = "유저네임은 필수 입력 값입니다.")
        @Length(min = 5, max = 12, message = "유저네임은 최소 5 이상, 12 이하의 길이를 가져야합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "유저네임은 영문과 숫자만 사용할 수 있습니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Length(min = 4, max = 20, message = "비밀번호는 최소 4 이상, 20 이하의 길이를 가져야합니다.")
        String password
) {
}
