package resource;

import dto.Test;
import dto.User;
import dto.response.SignInResponse;
import io.dropwizard.auth.Auth;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Range;
import service.UserService;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    @Path("lis")
    public List<Test> examListOnly() {
        return userService.listExam();
    }

    @GET
    @Path("list")
    public List<Test> examList(
            @Auth User user) {
        return userService.listExamForUser(user);
    }

    @POST
    @PermitAll
    @Path("enroll-exam")
    public void enroll(
            @Auth User user,
            @NotNull @FormParam("test_id") ObjectId testId) {
        userService.registerExam(user, testId);
    }
}
