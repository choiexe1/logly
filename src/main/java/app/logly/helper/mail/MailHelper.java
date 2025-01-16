package app.logly.helper.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class MailHelper {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void send(String to, String subject, String body) {
        MimeMessagePreparator messagePreparator =
                mimeMessage -> {
                    final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    helper.setFrom("noreply@noreply.com");
                    helper.setTo(to);
                    helper.setSubject(subject);
                    helper.setText(body, true);
                };

        mailSender.send(messagePreparator);
    }

    @Async("mailExecutor")
    public void sendVerifyCode(String to, int verificationCode) {
        Context context = new Context();

        context.setVariable("verificationCode", verificationCode);
        context.setVariable("to", to);

        String subject = "로글리 회원가입 인증 코드";
        String body = templateEngine.process("email/verifyCodeTemplate.html", context);
        send(to, subject, body);
    }

}
