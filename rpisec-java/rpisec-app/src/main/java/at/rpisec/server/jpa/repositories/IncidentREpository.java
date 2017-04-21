package at.rpisec.server.jpa.repositories;

import at.rpisec.server.jpa.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/21/17
 */
@Repository
public interface IncidentREpository extends JpaRepository<Incident, Long> {

}
