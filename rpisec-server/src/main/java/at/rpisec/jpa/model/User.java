package at.rpisec.jpa.model;

import at.rpisec.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/14/17
 */
@Entity
@Table(name = "USER", uniqueConstraints = {
        @UniqueConstraint(name = "uq_user_username", columnNames = "username"),
        @UniqueConstraint(name = "uq_user_email", columnNames = "email")
})
public class User extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "user_id_seq_generator", sequenceName = "user_id_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "user_id_seq_generator", strategy = GenerationType.SEQUENCE)
    @Getter
    @Setter
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "firstname")
    @Getter
    @Setter
    private String firstname;

    @NotNull
    @Size(max = 100)
    @Column(name = "lastname")
    @Getter
    @Setter
    private String lastname;

    @NotNull
    @Size(max = 100)
    @Column(name = "username")
    @Getter
    @Setter
    private String username;

    @NotNull
    @Size(max = 500)
    @Column(name = "password")
    @Getter
    @Setter
    private String password;

    @NotNull
    @Email
    @Size(max = 100)
    @Column(name = "email")
    @Getter
    @Setter
    private String email;

    @NotNull
    @Column(name = "admin", length = 1)
    @Getter
    @Setter
    private Boolean admin;
}
