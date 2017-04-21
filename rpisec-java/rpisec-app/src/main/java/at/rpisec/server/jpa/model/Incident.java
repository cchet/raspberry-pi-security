package at.rpisec.server.jpa.model;

import at.rpisec.server.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/21/17
 */
@Entity
@Table(name = "INCIDENT")
public class Incident extends BaseEntity<Long> {

    @Id
    @SequenceGenerator(name = "seq_incident", sequenceName = "seq_incident_id", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_incident", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    @Getter
    @Setter
    private Long id;

    @NotNull
    @Column(name = "OCCURRING_DATE", updatable = false)
    @Getter
    @Setter
    private LocalDateTime occurringDate;

    @Column(name = "VERIFICATION_DATE")
    @Getter
    @Setter
    private LocalDateTime verificationDate;

    @NotNull
    @Column(name = "ACTIVE", length = 1)
    @Getter
    @Setter
    private Boolean active = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    @Getter
    @Setter
    private User verificationUser;
}
