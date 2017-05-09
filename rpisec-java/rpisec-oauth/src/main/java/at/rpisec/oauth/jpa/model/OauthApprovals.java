package at.rpisec.oauth.jpa.model;

import at.rpisec.oauth.jpa.api.BaseEntity;
import at.rpisec.oauth.jpa.api.OauthApprovalState;
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
@Table(name = "approval")
@NoArgsConstructor
public class OauthApprovals extends BaseEntity<Long> {

    @NotNull
    @Size(max = 1024)
    @Getter
    @Setter
    @Id
    @SequenceGenerator(name = "seq_approval_id", sequenceName = "seq_approval_id", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_approval_id", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @Getter
    @Setter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Getter
    @Setter
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @NotNull
    @Getter
    @Setter
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @NotNull
    @Getter
    @Setter
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OauthApprovalState state;

    @NotNull
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private OauthClient client;

    @NotNull
    @Size(max = 1024)
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private OauthUser user;

    @NotNull
    @Size(max = 1024)
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "approval_scope", joinColumns = @JoinColumn(name = "approval_id", referencedColumnName = "id"))
    @Column(name = "scope")
    private Set<OauthScope> scope = new HashSet<>(0);
}
