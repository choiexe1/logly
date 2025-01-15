package app.logly.service;

import app.logly.domain.Member;
import app.logly.repository.MemberRepository;
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
        memberRepository.existsByUsername(member.getUsername());
        memberRepository.existsByNickname(member.getNickname());
        memberRepository.existsByEmail(member.getEmail());

        return memberRepository.save(member);
    }
}
