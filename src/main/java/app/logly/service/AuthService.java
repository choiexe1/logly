package app.logly.service;

import app.logly.domain.Member;
import app.logly.exception.InvalidPasswordException;
import app.logly.exception.UserNotFoundException;
import app.logly.helper.encryption.EncryptionHelper;
import java.util.random.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberService memberService;
    private final EncryptionHelper encryptionHelper;

    @Transactional
    public Member register(Member member) {
        memberService.validateExistsMember(member.getUsername(), member.getNickname(), member.getEmail());
        member.setPassword(encryptionHelper.hash(member.getPassword()));

        int randomNumber = RandomGenerator.of("Random").nextInt(10);
        member.setAvatarNumber(randomNumber);

        return memberService.save(member);
    }

    public Member authenticate(String username, String password) {
        Member member = memberService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("회원을 찾을 수 없습니다."));

        validatePassword(password, member.getPassword());

        return member;
    }

    @Transactional
    public void verifyEmail(Long memberId) {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException("회원을 찾을 수 없습니다."));

        member.setEmailVerified(true);
    }

    private void validatePassword(String rawPassword, String encryptedPassword) {
        if (!encryptionHelper.check(rawPassword, encryptedPassword)) {
            throw new InvalidPasswordException("패스워드가 일치하지 않습니다.");
        }
    }
}
