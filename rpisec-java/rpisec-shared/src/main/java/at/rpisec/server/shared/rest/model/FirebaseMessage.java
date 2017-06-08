package at.rpisec.server.shared.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/07/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirebaseMessage {

    private String to;
    private FirebaseMessageBody data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FirebaseMessageBody {

        private String title;
        private String message;
        private String dbItemId;
    }
}
