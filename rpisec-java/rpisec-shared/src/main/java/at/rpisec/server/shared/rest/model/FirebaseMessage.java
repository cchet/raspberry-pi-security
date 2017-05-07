package at.rpisec.server.shared.rest.model;

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
public class FirebaseMessage {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FirebaseMessageBody {

        private String title;
        private String message;
        private String dbItemId;
    }

    private String to;
    private FirebaseMessageBody data;
}
