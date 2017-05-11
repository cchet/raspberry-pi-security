package at.rpisec.server.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 05/11/17
 */
@Data
@NoArgsConstructor
public class ClientFormModel {

    @NotNull
    private String uuid;
    @NotNull
    private Long userId;
}
