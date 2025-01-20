package app.logly.web.form;

import jakarta.validation.constraints.NotBlank;

public record PostGuestbookForm(
        @NotBlank(message = "내용을 작성하세요.") String content
) {
}
