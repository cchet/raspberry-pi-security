package at.rpisec.auth.jpa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/17/17
 */
@Embeddable
@Data
@NoArgsConstructor
public class ClientDevice {

    @NotNull
    @Size(max = 1024)
    @Column(name = "client_id")
    private String clientId;

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

    public ClientDevice(String clientId) {
        this.clientId = clientId;
    }
}
