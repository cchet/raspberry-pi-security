package at.rpisec.server.logic.api;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/20/17
 */
public interface ClientLogic {

    Long create(String uuid,
                String username);

    void delete(String uuid,
                String username);
}
