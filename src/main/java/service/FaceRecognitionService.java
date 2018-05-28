package service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class FaceRecognitionService {

    private static final Logger logger = LoggerFactory.getLogger(FaceRecognitionService.class);
    private final Client client = ClientBuilder.newBuilder().
            register(MultiPartFeature.class).build();
    private final String API_SERVER = "http://localhost:5001";

    public FaceRecognitionService(
            Client client) {
        // this.client = client;
    }

    List<Double> trainImage(File photo) {
        try {
            MultiPart multiPart = null;
            multiPart = new MultiPart();

            FileDataBodyPart imgBodyPart = new FileDataBodyPart("photo", photo,
                    MediaType.APPLICATION_OCTET_STREAM_TYPE);
            multiPart.bodyPart(imgBodyPart);

            WebTarget server = client.target(API_SERVER + "/train");

            Response apiResponse = client.target(API_SERVER + "/train")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(multiPart, "multipart/form-data"));

            if (apiResponse.getStatus() != Response.Status.OK.getStatusCode()) {
                apiResponse.getEntity();

                throw new WebApplicationException("Unable to connect to server.");
            } else {
                String responseMessage = apiResponse.readEntity(String.class);
                Gson g = new Gson();
                TrainedResultResponse t = g.fromJson(responseMessage, TrainedResultResponse.class);
                List<Double> result = t.getModel();

                System.out.println(responseMessage);
                System.out.println(result);

                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception has occured " + e.getMessage());
        } finally {
//            if (null != multiPart) {
//                try {
//                    multiPart.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
        }
        return null;
    }

    Boolean verifyImage(File photo, List<Double> model) {
        MultiPart multiPart = null;
        multiPart = new MultiPart();

        HashMap<String, List<Double>> data = new HashMap<>();
        data.put("model", model);

        FileDataBodyPart imgBodyPart = new FileDataBodyPart("photo", photo,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        multiPart.bodyPart(imgBodyPart);
        multiPart.setEntity(data);

        WebTarget server = client.target(API_SERVER + "/verify");
        Response apiResponse = client.target(API_SERVER + "/verify")
                .request(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.html(multiPart));

        if (apiResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            apiResponse.getEntity();
            throw new WebApplicationException("Unable to connect to server.");
        } else {
            System.out.println("Questions downloaded");
        }
        return null;
    }

}

class TrainedResultResponse {
    private List<Double> model;

    @JsonCreator
    public TrainedResultResponse(
            @JsonProperty("model") List<Double> model
    ) {
        this.model = model;
    }

    public List<Double> getModel() {
        return model;
    }
}


class MatchingRequest {
    private Boolean verified;

    @JsonCreator
    public MatchingRequest(
            @JsonProperty("verified") Boolean verified
    ) {
        this.verified = verified;
    }

    public Boolean getVerified() {
        return verified;
    }
}
