package at.rpisec.oauth.jpa.model;

import at.rpisec.oauth.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/09/17
 */
@Entity
@Table(name = "access_token")
@NoArgsConstructor
public class OauthAccessToken extends BaseEntity<String> {

    @NotNull
    @Size(max = 1024)
    @Getter
    @Setter
    @Column(name = "id", updatable = false)
    private String id;

    @NotNull
    @Getter
    @Setter
    @Column(name = "token", length = 4096, updatable = false)
    private byte[] token;

    @NotNull
    @Size(max = 1024)
    @Getter
    @Setter
    @Column(name = "authentication_id", length = 1024, updatable = false)
    private String authenticationId;

    @NotNull
    @Getter
    @Setter
    @Column(name = "authentication", length = 4096, updatable = false)
    private byte[] authentication;

    @NotNull
    @Getter
    @Setter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private OauthUser user;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", updatable = false)
    public OauthClient client;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_id", updatable = false)
    public OauthRefreshToken refreshToken;

    public OauthAccessToken(String id,
                            byte[] token,
                            String authenticationId,
                            byte[] authentication,
                            OauthUser user,
                            OauthClient client) {
        this.id = id;
        this.token = token;
        this.authenticationId = authenticationId;
        this.authentication = authentication;
        this.user = user;
        this.client = client;
    }
}
