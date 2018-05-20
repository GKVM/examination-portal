package resource;

import dto.Test;
import dto.response.SignInResponse;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Range;
import service.UserService;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("candidate")
@Produces(MediaType.APPLICATION_JSON)
/*@Consumes(MediaType.APPLICATION_JSON)*/
public class CandidateResource {

    private UserService userService;

    public CandidateResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("signup")
    public SignInResponse signup(
            @NotNull @FormParam("name-first") String nameFirst,
            @NotNull @FormParam("name-last") String nameLast,
            @Range @NotNull @FormParam("phone") String phone,
            @Email @FormParam("email") String email,
            @NotNull @FormParam("password") String password
    ) {
        return userService.signup(nameFirst, nameLast, email, phone, password);
    }

    @POST
    @Path("signin")
    public SignInResponse signin(
            @NotNull @FormParam("phone-or-email") String phone,
            @NotNull @FormParam("password") String password
    ) {
        return userService.signin(phone, password);
    }

    @GET
    @PermitAll
    @Path("list")
    public List<Test> examList() {
        return userService.listExam();
    }

    @POST
    @PermitAll
    @Path("enroll-exam")
    public void enroll(@NotNull @FormParam("user_id") ObjectId userId,
                       @NotNull @FormParam("test_id") ObjectId testId) {
        userService.registerExam(null, null);
    }
}
