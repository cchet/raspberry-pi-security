package at.rpisec.server.shared.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/08/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseMessageResponse {

    private String multicast_id;
    private Boolean success;
    private Boolean failure;
    private String canonical_ids;
    private List<Map<String, String>> results;
}
