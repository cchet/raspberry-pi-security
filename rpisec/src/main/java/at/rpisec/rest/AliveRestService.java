package at.rpisec.rest;

import at.rpisec.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Thomas Herzog <t.herzog@curecomp.com>
 * @since 04/14/17
 */
@Component
@Path("/admin")
public class AliveRestService {

    @Path("/alive")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String isAlive(){
        return "alive";
    }

}
