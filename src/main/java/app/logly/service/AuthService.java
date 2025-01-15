package app.logly.service;

import app.logly.domain.Member;
import app.logly.encryption.EncryptionHelper;
import app.logly.exception.InvalidPasswordException;
import app.logly.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberService memberService;
    private final EncryptionHelper encryptionHelper;

    public Member register(Member member) {
        memberService.validateExistsMember(member.getUsername(), member.getNickname(), member.getEmail());
        member.setPassword(encryptionHelper.hash(member.getPassword()));
        return memberService.save(member);
    }

    public void authenticate(String username, String password) {
        Member member = memberService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("회원을 찾을 수 없습니다."));

        validatePassword(password, member.getPassword());
    }

    private void validatePassword(String rawPassword, String encryptedPassword) {
        if (!encryptionHelper.check(rawPassword, encryptedPassword)) {
            throw new InvalidPasswordException("패스워드가 일치하지 않습니다.");
        }
    }

}
