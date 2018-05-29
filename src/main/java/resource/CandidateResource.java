package resource;

import dto.Test;
import dto.User;
import dto.response.LoginToExam;
import dto.response.SignInResponse;
import io.dropwizard.auth.Auth;
import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Range;
import service.UserService;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;

//import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.core.header.FormDataContentDisposition;
//

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


    @POST
    @Path("upload-photo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response getPhoto(
            @Auth User user,
            @FormDataParam("photo") final InputStream fileInputStream,
            @FormDataParam("photo") final FormDataContentDisposition fileDetail
    ) {
        String uploadedFileLocation = "images/" + System.currentTimeMillis() + "--" + fileDetail.getFileName();
        System.out.println(fileDetail.getFileName());
        // save it
        try {
            File imageFile = new File(uploadedFileLocation);
            FileUtils.copyInputStreamToFile(fileInputStream, imageFile);
            userService.saveImage(user, imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output = "File uploaded to : " + uploadedFileLocation;

        return Response.status(200).build();
    }


    @GET
    @Path("mock-signin")
    public LoginToExam loginForExamMock(
            @Auth User user,
            @QueryParam("registration") String registrationId,
            @QueryParam("password") String password
    ) {
        return userService.mockLogin(user, registrationId, password);
    }


    private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        int read;
        final int BUFFER_LENGTH = 1024;
        final byte[] buffer = new byte[BUFFER_LENGTH];
        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
        out.close();
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
