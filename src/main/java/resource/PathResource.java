package resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class PathResource {

    public PathResource() {

    }

    @GET
    @Path("/")
    public InputStream getHome() {
        return getClass().getResourceAsStream("/assets/index.html");
    }

    @GET
    @Path("list")
    public InputStream getList() {
        return getClass().getResourceAsStream("/assets/list.html");
    }

    @GET
    @Path("test")
    public InputStream getTest() {
        return getClass().getResourceAsStream("/assets/exam.html");
    }
}
