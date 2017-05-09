package at.rpisec.oauth.jpa.model;

import at.rpisec.oauth.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Entity
@Table(name = "oauth_code")
@NoArgsConstructor
public class OauthCode extends BaseEntity<String> {

    @NotNull
    @Getter
    @Setter
    @Id
    @Column(name = "id", updatable = false)
    private String id;

    @NotNull
    @Getter
    @Setter
    @Lob
    @Column(name = "authentication", updatable = false)
    private byte[] authentication;

    @NotNull
    @Getter
    @Setter
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public OauthCode(String id,
                     byte[] authentication) {
        this.id = id;
        this.authentication = authentication;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
