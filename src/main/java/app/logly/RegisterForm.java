package app.logly;

public record RegisterForm(
        String username,
        String nickname,
        String email,
        String password,
        Boolean subscribeNewsletter
) {
}
