package at.rpisec.oauth.jpa.model;

import at.rpisec.oauth.jpa.api.BaseEntity;
import at.rpisec.oauth.jpa.api.OauthEntityState;
import at.rpisec.oauth.jpa.api.OauthScope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "client")
@NoArgsConstructor
public class OauthClient extends BaseEntity<Long> {

    @Getter
    @Setter
    @Id
    @SequenceGenerator(name = "seq_client_id", sequenceName = "seq_client_id", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_client_id", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @Size(min = 36, max = 1024)
    @Getter
    @Setter
    @Column(name = "client_id", updatable = false, unique = true)
    private String clientId;

    @NotNull
    @Size(min = 36, max = 1024)
    @Getter
    @Setter
    @Column(name = "client_secret")
    private String clientSecret;

    @Size(max = 4096)
    @Getter
    @Setter
    @Column(name = "additional_information")
    private String additionalInformation;

    @NotNull
    @Getter
    @Setter
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Getter
    @Setter
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Getter
    @Setter
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @NotNull
    @Getter
    @Setter
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OauthEntityState state = OauthEntityState.ACTIVE;

    @NotNull
    @Size(max = 255)
    @Getter
    @Setter
    @Column(name = "authorized_grant_types")
    private Set<OauthScope> supportedScopes = new HashSet<>(0);

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private Set<OauthAccessToken> accessTokens = new HashSet<>(0);

    @PrePersist
    public void prePersist() {
        createdAt = modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        modifiedAt = LocalDateTime.now();
    }

}
