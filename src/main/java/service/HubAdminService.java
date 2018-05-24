package service;

import dao.QuestionDao;
import dao.ResponseDao;
import dao.TestDao;
import dao.UserDao;
import dto.Question;
import dto.Test;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

public class HubAdminService {

    private final Client client;
    private final String CENTRAL_SERVER;
    private static final Logger logger = LoggerFactory.getLogger(HubService.class);

    private final TestDao testDao;
    private final UserDao userDao;
    private final QuestionDao questionDao;
    private final ResponseDao responseDao;

    public HubAdminService(
            TestDao testDao,
            UserDao userDao,
            QuestionDao questionDao,
            ResponseDao responseDao,
            final Client client,
            String central_server) {
        this.testDao = testDao;
        this.userDao = userDao;
        this.questionDao = questionDao;
        this.responseDao = responseDao;
        this.client = client;
        CENTRAL_SERVER = central_server;
    }

    public void fetchQuestions(ObjectId testId) {
        Test test = testDao.getTest(testId).orElseThrow(() -> new WebApplicationException("Test id is invalid"));
        HashMap<String, String> data = new HashMap<>();
        data.put("test_id", testId.toString());
        requestPOST("/server/questions", data);
        System.out.println("Questions downloaded");

    }

    public void fetchAttendeeData(ObjectId testId) {
        Test test = testDao.getTest(testId).orElseThrow(() -> new WebApplicationException("Test id is invalid"));
        HashMap<String, String> data = new HashMap<>();
        data.put("test_id", testId.toString());
        requestPOST("/server/candidates", data);
    }

    public void submitResponse(ObjectId testId) {
        Test test = testDao.getTest(testId).orElseThrow(() -> new WebApplicationException("Test id is invalid"));
        HashMap<String, String> data = new HashMap<>();
        data.put("test_id", testId.toString());
    }

    private void requestPOST(String url, HashMap<String, String> data) {
        Response questionResponse = client.target(CENTRAL_SERVER + url)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(data));
        if (questionResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            questionResponse.getEntity();
            throw new WebApplicationException("Unable to connect to main server to fetch questions.");
        } else {
            System.out.println("Questions downloaded");
        }
    }
}
