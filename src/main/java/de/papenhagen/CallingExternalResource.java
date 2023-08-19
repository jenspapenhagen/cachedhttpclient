package de.papenhagen;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/rest")
public class CallingExternalResource {

    @Inject
    CallingExternalService callingExternalService;

    @Inject
    CacheClearer cacheClearer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String call(@RestQuery String parameter) {
        //Testing http://localhost:8080/rest?parameter=9781451648546

        return callingExternalService.getResponseFromExternalService(parameter);
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String cleanCache() {
        cacheClearer.clearCache("external_cache");
        return "External Cache cleaned";
    }

    //

}
