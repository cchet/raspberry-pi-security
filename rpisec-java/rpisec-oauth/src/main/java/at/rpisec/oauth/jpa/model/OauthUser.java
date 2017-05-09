package at.rpisec.oauth.jpa.model;

import at.rpisec.oauth.jpa.api.BaseEntity;
import at.rpisec.oauth.jpa.api.OauthEntityState;
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
@Table(name = "user")
@NoArgsConstructor
public class OauthUser extends BaseEntity<Long> {

    @Getter
    @Setter
    @Id
    @SequenceGenerator(name = "seq_user_id", sequenceName = "seq_user_id", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_user_id", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @Size(min = 2, max = 1024)
    @Getter
    @Setter
    @Column(name = "username", updatable = false, unique = true)
    private String username;

    @NotNull
    @Size(min = 5, max = 1024)
    @Getter
    @Setter
    @Column(name = "email", updatable = false, unique = true)
    private String email;

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

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<OauthApprovals> approvals = new HashSet<>(0);

    @PrePersist
    public void prePersist() {
        createdAt = modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        modifiedAt = LocalDateTime.now();
    }
}
