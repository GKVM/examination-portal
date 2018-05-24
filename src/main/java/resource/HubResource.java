package resource;

import dto.Questions;
import dto.Responses;
import dto.response.UserDetailed;
import org.bson.types.ObjectId;
import service.HubService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Endpoints which the hub uses to connect to server.
 */
@Path("/hub")
public class HubResource {
    private final HubService hubService;

    public HubResource(
            HubService hubService
    ) {
        this.hubService = hubService;
    }

    /**
     * Loads the question paper for the test.
     */
    @GET
    @Path("users")
    public Questions loadQuestions(
            @QueryParam("test_id") ObjectId id
    ) {
        // TODO: 23/5/18 Send request-> download question json ->
        return hubService.getQuestions(id);
    }

    /**
     * Loads candidate info of all people designated to write the exam in the center.
     */
    @GET
    @Path("registered")
    public List<UserDetailed> loadCandidateInfo(
            @QueryParam("test_id") ObjectId id
    ) {
        return hubService.getUserDetailsForTest(id);
    }

    /**
     * Gets the response made by candidates to the server.
     */
    @POST
    @Path("upload_answers")
    public Response uploadResponse(List<Responses> responsesList) {
         hubService.saveResponse(responsesList);
         return Response.ok().build();
    }
}
