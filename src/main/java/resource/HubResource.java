package resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/*
 * Endpoints which the hub uses connect to server.
 */
@Path("/hub")
public class HubResource {
    @GET
    public Response loadQuestions() {
        return null;
    }
}
