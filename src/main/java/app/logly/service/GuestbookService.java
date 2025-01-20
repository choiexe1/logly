package app.logly.service;

import app.logly.domain.Guestbook;
import app.logly.repository.GuestbookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GuestbookService {
    private final GuestbookRepository guestbookRepository;

    @Transactional
    public Guestbook save(Guestbook guestBook) {
        return guestbookRepository.save(guestBook);
    }

    public List<Guestbook> findAllByHostId(Long memberId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return guestbookRepository.findAllByHostId(memberId, sort);
    }

    public List<Guestbook> findAllByGuestId(Long memberId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return guestbookRepository.findAllByGuestId(memberId, sort);
    }
}
