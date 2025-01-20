package app.logly.repository;

import app.logly.domain.Guestbook;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {
    List<Guestbook> findAllByHostId(Long memberId, Sort sort);

    List<Guestbook> findAllByGuestId(Long memberId, Sort sort);
}
