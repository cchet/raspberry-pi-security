package at.rpisec.oauth.jpa.model;

import at.rpisec.oauth.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Entity
@Table(name = "refresh_token")
@NoArgsConstructor
public class OauthRefreshToken extends BaseEntity<String> {

    @NotNull
    @Getter
    @Setter
    @Id
    @Column(name = "id", updatable = false)
    private String id;

    @NotNull
    @Getter
    @Setter
    @Column(name = "token", length = 4096, updatable = false)
    private byte[] token;

    @NotNull
    @Getter
    @Setter
    @Column(name = "authentication", length = 4096, updatable = false)
    private byte[] authentication;

    public OauthRefreshToken(String id,
                             byte[] token,
                             byte[] authentication) {
        this.id = id;
        this.token = token;
        this.authentication = authentication;
    }
}
