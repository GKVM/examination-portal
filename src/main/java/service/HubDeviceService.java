package service;

import dao.*;
import dto.Questions;
import dto.Registration;
import dto.Responses;
import dto.User;
import dto.response.LoginToExam;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class HubDeviceService {

    private static final Logger logger = LoggerFactory.getLogger(HubService.class);

    private final TestDao testDao;
    private final UserDao userDao;
    private final QuestionDao questionDao;
    private final ResponseDao responseDao;
    private final RegistrationDao registrationDao;

    public HubDeviceService(
            TestDao testDao,
            UserDao userDao,
            QuestionDao questionDao,
            ResponseDao responseDao,
            RegistrationDao registrationDao
    ) {
        this.testDao = testDao;
        this.userDao = userDao;
        this.questionDao = questionDao;
        this.responseDao = responseDao;
        this.registrationDao = registrationDao;
    }

    public LoginToExam login(String registration, String password) {
        final Registration registrationInfo = registrationDao.getRegistration(registration)
                .orElseThrow(() -> new WebApplicationException("Invalid credentials", javax.ws.rs.core.Response.Status.UNAUTHORIZED));
        if (!password.equals(registrationInfo.getPassword())) {
            throw new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED);
        }

        final User user = userDao.getUser(registrationInfo.getUserId()).orElseThrow(() -> new WebApplicationException("User not found."));

        return new LoginToExam(
                registrationInfo.getId(),
                "name todo",
                null,
                null,
                "",
                ""
        );
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
