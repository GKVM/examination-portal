package resource;

import dto.Questions;
import dto.response.LoginToExam;
import org.bson.types.ObjectId;
import service.HubDeviceService;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("device")
public class HubDeviceResource {

    private final HubDeviceService hubDeviceService;

    public HubDeviceResource(HubDeviceService hubDeviceService) {
        this.hubDeviceService = hubDeviceService;
    }

    @POST
    public LoginToExam loginForExam(
            @QueryParam("registration") String registrationId,
            @QueryParam("password") String password
    ){
        return hubDeviceService.login(registrationId,password);
    }

    @GET
    @Path("questions")
    public Questions downloadQuestions(@Valid @QueryParam("test") ObjectId testId) {
        return hubDeviceService.getQuestions(testId);
    }

    @POST
    @Path("sent-reply")
    public Response submitAnswer(
            dto.Response response
    ){
        /*hubDeviceService*/
        return Response.ok().build();
    }

    @POST
    @Path("send-all-reply")
    public Response submitTest(List<Response> responsesList){
        hubDeviceService.saveFullResponse(responsesList);
        return Response.ok().build();
    }
}
