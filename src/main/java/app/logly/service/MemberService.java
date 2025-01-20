package app.logly.service;

import app.logly.domain.Member;
import app.logly.exception.EmailInUsedException;
import app.logly.exception.NicknameInUsedException;
import app.logly.exception.UserNotFoundException;
import app.logly.exception.UsernameInUsedException;
import app.logly.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Member findByIdOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("회원을 찾을 수 없습니다."));
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public void validateExistsMember(String username, String nickname, String email) {
        if (memberRepository.existsByUsername(username)) {
            throw new UsernameInUsedException("이미 사용중인 유저네임입니다.");
        }
        if (memberRepository.existsByNickname(nickname)) {
            throw new NicknameInUsedException("이미 사용중인 닉네임입니다.");
        }
        if (memberRepository.existsByEmail(email)) {
            throw new EmailInUsedException("이미 사용중인 이메일입니다.");
        }
    }

    @Transactional
    public void update(Long id, String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new NicknameInUsedException("이미 사용중인 닉네임입니다.");
        }
        
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("회원을 찾을 수 없습니다."));

        member.updateNickname(nickname);
    }
}
