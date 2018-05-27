package resource;

import dto.Questions;
import dto.ResponseModel;
import dto.response.LoginToExam;
import org.bson.types.ObjectId;
import service.HubDeviceService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("device")
@Produces(MediaType.APPLICATION_JSON)
public class HubDeviceResource {

    private final HubDeviceService hubDeviceService;

    public HubDeviceResource(HubDeviceService hubDeviceService) {
        this.hubDeviceService = hubDeviceService;
    }

    @GET
    @Path("login")
    public LoginToExam loginForExam(
            @QueryParam("registration") String registrationId,
            @QueryParam("password") String password
    ){
        return hubDeviceService.login(registrationId, password);
    }

    @GET
    @Path("questions")
    public Questions downloadQuestions(@Valid @QueryParam("test") ObjectId testId) {
        return hubDeviceService.getQuestions(testId);
    }

    @POST
    @Path("submit-response")
    public Response submitAnswer(
            ResponseModel response,
            @QueryParam("user_id") ObjectId userId,
            @QueryParam("test_id") ObjectId testId
    ){
        hubDeviceService.saveResponse(response, testId, userId);
        return Response.ok().build();
    }

    @POST
    @Path("submit-all-responses")
    public Response submitTest(List<ResponseModel> responsesList,
                               @QueryParam("user_id") ObjectId userId,
                               @QueryParam("test_id") ObjectId testId){
        hubDeviceService.saveFullResponse(responsesList, testId, userId);
        return Response.ok().build();
    }
}
