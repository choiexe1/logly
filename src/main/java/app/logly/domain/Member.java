package app.logly.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Setter
    private String password;

    @Setter
    private boolean emailVerified;
    
    private boolean subscribeNewsletter;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Member of(String username, String nickname, String email, String password,
                            Boolean subscribeNewsletter) {
        Member member = new Member();
        member.username = username;
        member.nickname = nickname;
        member.email = email;
        member.password = password;
        member.createdAt = LocalDateTime.now();
        member.subscribeNewsletter = subscribeNewsletter;
        member.emailVerified = false;

        return member;
    }
}
