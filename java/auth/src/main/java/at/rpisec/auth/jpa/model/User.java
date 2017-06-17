package at.rpisec.auth.jpa.model;

import at.rpisec.auth.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Theis entity represents an registered user.
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/14/17
 */
@Entity
@Table(name = "domain_user", uniqueConstraints = {
        @UniqueConstraint(name = "uq_domain_user_username", columnNames = "USERNAME"),
        @UniqueConstraint(name = "uq_domain_user_email", columnNames = "EMAIL")
})
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "user_id_seq_generator", sequenceName = "domain_user_id_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "user_id_seq_generator", strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "firstname")
    private String firstname;

    @NotNull
    @Size(max = 100)
    @Column(name = "lastname")
    private String lastname;

    @NotNull
    @Size(max = 100)
    @Column(name = "username")
    private String username;

    @NotNull
    @Size(max = 500)
    @Column(name = "password")
    private String password;

    @NotNull
    @Email
    @Size(max = 100)
    @Column(name = "email")
    private String email;

    @Size(max = 255)
    @Column(name = "verify_uuid")
    private String verifyUUID;

    @NotNull
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @NotNull
    @Column(name = "password_validity_date")
    private LocalDateTime passwordValidityDate;

    @NotNull
    @Column(name = "admin", length = 1)
    private Boolean admin;

    @NotNull
    @Column(name = "deactivated", length = 1)
    private Boolean deactivated = Boolean.FALSE;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "domain_user_roles", joinColumns = @JoinColumn(name = "domain_user_id", referencedColumnName = "id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>(0);

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "user_client")
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "client_id", length = 1024)
    private Map<String, ClientDevice> clientDevices = new HashMap<>(0);

    @PrePersist
    public void prePersist() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
