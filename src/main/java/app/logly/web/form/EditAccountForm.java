package app.logly.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EditAccountForm {
    @Length(min = 3, max = 10, message = "닉네임은 최소 3 이상, 최대 10 이하의 길이를 가져야합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_]+$", message = "닉네임은 영문, 한글, 숫자, 언더바(_)만 사용할 수 있습니다.")
    private String nickname;

    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "유효한 이메일 주소를 입력해주세요.")
    private String email;
}