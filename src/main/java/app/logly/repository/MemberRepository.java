package app.logly.repository;

import app.logly.domain.Member;
import jakarta.persistence.EntityExistsException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username) throws EntityExistsException;

    boolean existsByEmail(String email) throws EntityExistsException;

    boolean existsByNickname(String nickname) throws EntityExistsException;
}
