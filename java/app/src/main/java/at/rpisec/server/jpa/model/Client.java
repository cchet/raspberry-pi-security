package at.rpisec.server.jpa.model;

import at.rpisec.server.jpa.api.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class Client extends AbstractEntity<ClientId> {

    @NotNull
    @EmbeddedId
    private ClientId id;

    @Size(max = 1024)
    @Column(name = "fcm_token")
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
