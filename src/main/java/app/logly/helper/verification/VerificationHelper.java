package app.logly.helper.verification;

import app.logly.exception.VerificationCodeNotMatchException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/**
 * VerificationHelper는 이메일 검증을 위한 헬퍼 클래스입니다.
 */
@Component
public class VerificationHelper {
    private final ConcurrentHashMap<Long, Integer> store = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private static final int VALID_MINUTES = 5;

    public void setVerificationCode(Long memberId, int verificationCode) {
        store.put(memberId, verificationCode);
        executorService.schedule(() -> store.remove(memberId), VALID_MINUTES, TimeUnit.MINUTES);
    }

    public Optional<Integer> getVerificationCode(Long memberId) {
        return Optional.ofNullable(store.get(memberId));
    }

    public void removeVerificationCode(Long memberId) {
        store.remove(memberId);
    }

    public int generateVerificationCode(Long memberId) {
        Random random = new Random();
        int verificationCode = random.nextInt(9000) + 1000;

        setVerificationCode(memberId, verificationCode);
        return verificationCode;
    }

    public void validateVerificationCodeMatch(Long memberId, int verificationCode) {
        Integer code = getVerificationCode(memberId)
                .orElseThrow(() -> new VerificationCodeNotMatchException("인증 코드가 존재하지 않습니다."));

        if (code != verificationCode) {
            throw new VerificationCodeNotMatchException("인증 코드가 일치하지 않습니다.");
        }
    }
}