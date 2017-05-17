package at.rpisec.server.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/17/17
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientId implements Serializable {

    private String clientId;
    private Long userId;
}
