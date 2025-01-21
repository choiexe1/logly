package app.logly;

import app.logly.domain.Member;
import app.logly.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class DevDataInitializer {
    private final AuthService authService;

    @EventListener(ApplicationReadyEvent.class)
    public void memberInit() {
        Member member = Member
                .of("test", "test", "test@test.com",
                        "test", false);

        member.setEmailVerified(true);

        Member member2 = Member
                .of("test1", "test1", "test1@test.com",
                        "test1", false);
        member2.setEmailVerified(true);

        authService.register(member);
        authService.register(member2);
    }
}
