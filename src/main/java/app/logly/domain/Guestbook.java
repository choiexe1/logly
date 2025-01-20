package app.logly.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guest_books")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Guestbook {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Member guest;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Guestbook of(String content) {
        Guestbook guestbook = new Guestbook();
        guestbook.content = content;
        guestbook.createdAt = LocalDateTime.now();
        return guestbook;
    }

    public void setHost(Member host) {
        this.host = host;
        host.getReceivedGuestbook().add(this);
    }

    public void setGuest(Member guest) {
        this.guest = guest;
        host.getPostedGuestbook().add(this);
    }

    @PreUpdate
    private void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
