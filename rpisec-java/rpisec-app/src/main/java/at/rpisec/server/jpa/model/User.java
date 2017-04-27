package at.rpisec.server.jpa.model;

import at.rpisec.server.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/14/17
 */
@Entity
@Table(name = "DOMAIN_USER", uniqueConstraints = {
        @UniqueConstraint(name = "uq_domain_user_username", columnNames = "USERNAME"),
        @UniqueConstraint(name = "uq_domain_user_email", columnNames = "EMAIL")
})
@NoArgsConstructor
public class User extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "user_id_seq_generator", sequenceName = "domain_user_id_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "user_id_seq_generator", strategy = GenerationType.SEQUENCE)
    @Getter
    @Setter
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "FIRSTNAME")
    @Getter
    @Setter
    private String firstname;

    @NotNull
    @Size(max = 100)
    @Column(name = "LASTNAME")
    @Getter
    @Setter
    private String lastname;

    @NotNull
    @Size(max = 100)
    @Column(name = "USERNAME")
    @Getter
    @Setter
    private String username;

    @NotNull
    @Size(max = 500)
    @Column(name = "PASSWORD")
    @Getter
    @Setter
    private String password;

    @NotNull
    @Email
    @Size(max = 100)
    @Column(name = "EMAIL")
    @Getter
    @Setter
    private String email;

    @Size(max = 255)
    @Column(name = "VERIFY_UUID")
    @Getter
    @Setter
    private String verifyUUID;

    @NotNull
    @Column(name = "CREATED", updatable = false)
    @Getter
    @Setter
    private LocalDateTime createdDate;

    @NotNull
    @Column(name = "UPDATED")
    @Getter
    @Setter
    private LocalDateTime updatedDate;

    @Column(name = "VERIFY_DATE")
    @Getter
    @Setter
    private LocalDateTime verifyDate;

    @NotNull
    @Column(name = "ADMIN", length = 1)
    @Getter
    @Setter
    private Boolean admin;

    @Getter
    @Setter
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Client> clients = new HashSet<>(0);

    @PrePersist
    public void prePersist() {
        createdDate = updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
