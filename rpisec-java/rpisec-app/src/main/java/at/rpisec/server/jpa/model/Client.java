package at.rpisec.server.jpa.model;

import at.rpisec.server.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class Client extends BaseEntity<String> {

    @NotNull
    @Id
    @Column(name = "id")
    private String id;

    @NotNull
    @Column(name = "user_id", length = 1024)
    private Long userId;

    @Column(name = "fcm_token", length = 1024)
    private String fcmToken;

    @NotNull
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
