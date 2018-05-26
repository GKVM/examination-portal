package service;

import dao.*;
import dto.Questions;
import dto.Registration;
import dto.Responses;
import dto.response.LoginToExam;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.List;

public class HubDeviceService {

    private final Client client;
    private static final Logger logger = LoggerFactory.getLogger(HubService.class);

    private final TestDao testDao;
    private final UserDao userDao;
    private final QuestionDao questionDao;
    private final ResponseDao responseDao;

    public HubDeviceService(
            TestDao testDao,
            UserDao userDao,
            QuestionDao questionDao,
            ResponseDao responseDao,
            final Client client) {
        this.testDao = testDao;
        this.userDao = userDao;
        this.questionDao = questionDao;
        this.responseDao = responseDao;
        this.client = client;
    }


    public LoginToExam login(String registration, String password) {
        // TODO: 26/5/18  
        //Registration registrationObj = registrationDao.;
        return null;
    }

    public Questions getQuestions(ObjectId testId) {
        Questions questions = questionDao.getQuestionSet(testId).get();
        return questions;
    }

    public Integer saveResponse(ObjectId testId, ObjectId userId, dto.Response response) {
        Responses responses = responseDao.fetchResponsesForQuestionSet(testId, userId);

        return null;
    }

    public Integer saveFullResponse(List<Response> responses) {
        return 3;
    }

}
