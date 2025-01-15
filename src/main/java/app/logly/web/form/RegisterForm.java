package app.logly.web.form;

import jakarta.validation.constraints.NotBlank;

public record RegisterForm(
        @NotBlank String username,
        @NotBlank String nickname,
        @NotBlank String email,
        @NotBlank String password,
        Boolean subscribeNewsletter
) {
}
