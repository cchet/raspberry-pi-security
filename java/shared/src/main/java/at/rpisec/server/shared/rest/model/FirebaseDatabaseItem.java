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
public class FirebaseDatabaseItem {

    public String message;
    public Boolean verified;
    public String dataType;
    public String base64Data;
}
