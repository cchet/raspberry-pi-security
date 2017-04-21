package at.rpisec.server.jpa.model;

import at.rpisec.server.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/19/17
 */
@Entity
@Table(name = "CLIENT", uniqueConstraints = {
        @UniqueConstraint(name = "uq_client_uuid_user_id", columnNames = {"UUID", "USER_ID"})
})
public class Client extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "client_id_seq_generator", sequenceName = "client_id_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "client_id_seq_generator", strategy = GenerationType.SEQUENCE)
    @Getter
    @Setter
    private Long id;

    @NotNull
    @Column(name = "UUID")
    @Getter
    @Setter
    private String uuid;

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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", updatable = false)
    @Getter
    @Setter
    private User user;

    @PrePersist
    public void prePersist() {
        createdDate = updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
