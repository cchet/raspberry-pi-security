package at.rpisec.jpa.model;

import at.rpisec.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/19/17
 */
@Entity
@Table(name = "CLIENT")
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", updatable = false)
    @Getter
    @Setter
    private User user;
}
