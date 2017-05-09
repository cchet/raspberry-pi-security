package at.rpisec.oauth.jpa.model;

import at.rpisec.oauth.jpa.api.BaseEntity;
import at.rpisec.oauth.jpa.api.OauthEntityState;
import at.rpisec.oauth.jpa.api.OauthScope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Entity
@Table(name = "client_token")
@NoArgsConstructor
public class OauthClientToken extends BaseEntity<Long> {

    @NotNull
    @Getter
    @Setter
    @Id
    @Column(name = "client_id", updatable = false)
    private Long id;

    @NotNull
    @Getter
    @Setter
    @Column(name = "token", length = 4096, updatable = false, unique = true)
    @Lob
    private byte[] token;

    @NotNull
    @Getter
    @Setter
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", updatable = false, insertable = false)
    private OauthClient client;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public OauthClientToken(Long id,
                            byte[] token,
                            OauthClient client) {
        this.id = id;
        this.token = token;
        this.client = client;
    }
}
