package app.logly.service;

import app.logly.exception.VerificationCodeNotMatchException;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private static final String CACHE_NAME = "verification";
    private final CacheManager cacheManager;

    @Cacheable(cacheNames = CACHE_NAME, key = "#memberId")
    public int generateVerificationCode(Long memberId) {
        return randomVerificationCode();
    }

    @CachePut(cacheNames = CACHE_NAME, key = "#memberId")
    public int regenerateVerificationCode(Long memberId) {
        return randomVerificationCode();
    }

    public boolean verify(Long memberId, int verificationCode) {
        Cache cache = cacheManager.getCache(CACHE_NAME);

        Integer cachedVerificationCode = Optional
                .ofNullable(cache.get(memberId))
                .map(Cache.ValueWrapper::get)
                .map(Integer.class::cast)
                .orElseThrow(() -> new VerificationCodeNotMatchException("인증 코드가 존재하지 않습니다."));

        if (verificationCode == cachedVerificationCode) {
            cache.evictIfPresent(memberId);
            return true;
        } else {
            throw new VerificationCodeNotMatchException("인증 코드가 일치하지 않습니다.");
        }
    }

    private int randomVerificationCode() {
        return new Random().nextInt(9000) + 1000;
    }
}
