package service;

import dao.*;
import dto.*;
import dto.response.LoginToExam;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;

public class HubDeviceService {

    private static final Logger logger = LoggerFactory.getLogger(HubService.class);

    private final ExaminationDao examinationDao;
    private final UserDao userDao;
    private final QuestionDao questionDao;
    private final ResponseDao responseDao;
    private final RegistrationDao registrationDao;
    private final FaceRecognitionService faceRecognitionService;

    public HubDeviceService(
            ExaminationDao examinationDao,
            UserDao userDao,
            QuestionDao questionDao,
            ResponseDao responseDao,
            RegistrationDao registrationDao,
            FaceRecognitionService faceRecognitionService
    ) {
        this.examinationDao = examinationDao;
        this.userDao = userDao;
        this.questionDao = questionDao;
        this.responseDao = responseDao;
        this.registrationDao = registrationDao;
        this.faceRecognitionService = faceRecognitionService;
    }

    public LoginToExam login(String registration, String password) {
        Registration registrationInfo = registrationDao.getRegistration(registration)
                .orElseThrow(() -> new WebApplicationException("Invalid credentials", javax.ws.rs.core.Response.Status.UNAUTHORIZED));
        if (!password.equals(registrationInfo.getPassword())) {
            throw new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED);
        }

        final User user = userDao.getUser(registrationInfo.getUserId()).orElseThrow(() -> new WebApplicationException("User not found."));

        return new LoginToExam(
                registrationInfo.getId(),
                registrationInfo.getTestId(),
                null,
                user.getCompleteName(),
                user.getEmail(),
                user.getPhone(),
                "",
                ""
        );
    }

    public Questions getQuestions(ObjectId testId) {
        Questions questions = questionDao.getQuestionSet(testId).get();
        return questions;
    }

    public Integer saveResponse(ResponseModel response, ObjectId testId, ObjectId userId) {
        Responses responses = responseDao.fetchResponsesForQuestionSet(testId, userId).get();
        // TODO: 27/5/18 Add response.
        responses.getResponses().add(response);
        return null;
    }

    public Integer saveFullResponse(List<ResponseModel> responses, ObjectId testId, ObjectId userId) {
        Responses responsesObj = new Responses(new ObjectId(),
                testId,
                userId,
                responses
        );
        responseDao.saveResponse(responsesObj);
        return 3;
    }

    public Boolean authenticate(ObjectId userId, File file) {
        System.out.println("Writing");
        User user = userDao.getUser(userId).orElseThrow(() -> new WebApplicationException(""));
        Boolean result = faceRecognitionService.verifyImage(file, user.getModel());
        return result;
    }

}
