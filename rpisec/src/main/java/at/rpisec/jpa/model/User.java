package at.rpisec.jpa.model;

import at.rpisec.jpa.api.BaseEntity;
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
        @UniqueConstraint(name = "uq_user_email", columnNames = "email"),
        @UniqueConstraint(name = "uq_user_admin", columnNames = "admin")
})
public class User extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "user_id_seq_generator", sequenceName = "user_id_sequence", initialValue = 1, allocationSize = 1)
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

    @NotNull
    @Column(name = "admin", length = 1)
    private Boolean admin;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
