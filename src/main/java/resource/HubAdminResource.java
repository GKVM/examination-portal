package resource;


import org.bson.types.ObjectId;
import service.HubAdminService;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("admin_hub")
public class HubAdminResource {

    private final HubAdminService hubAdminService;

    public HubAdminResource(HubAdminService hubAdminService) {
        this.hubAdminService = hubAdminService;
    }

    @GET
    @Path("questions")
    public Response downloadQuestions(@Valid @QueryParam("test") ObjectId testId) {
        hubAdminService.fetchQuestions(testId);
        return Response.ok().build();
    }

    @GET
    @Path("users")
    public Response downloadUserInfo(@Valid @QueryParam("test") ObjectId testId) {
        hubAdminService.fetchAttendeeData(testId);
        return Response.ok().build();
    }

    @GET
    @Path("response")
    public Response uploadQuestions(@Valid @QueryParam("test") ObjectId testId) {
        hubAdminService.submitResponse(testId);
        return Response.ok().build();
    }
}
