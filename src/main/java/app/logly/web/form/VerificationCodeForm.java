package app.logly.web.form;

public record VerificationCodeForm(
        Integer first,
        Integer second,
        Integer third,
        Integer fourth
) {
    public int merge() {
        return first * 1000 + second * 100 + third * 10 + fourth;
    }
}
