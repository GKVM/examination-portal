package service;

import dao.*;
import dto.*;
import dto.response.Authenticated;
import dto.response.LoginToExam;
import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
                registrationInfo.getUserId(),
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

    public Authenticated authenticate(ObjectId userId, InputStream fileInputStream,
                                      FormDataContentDisposition fileDetail) {
        // save it
        try {
            System.out.println("User id: " + userId);
            String uploadedFileLocation = "images/" + System.currentTimeMillis() + "--" + fileDetail.getFileName();
            System.out.println(fileDetail.getFileName());

            File file = new File(uploadedFileLocation);
            FileUtils.copyInputStreamToFile(fileInputStream, file);

            System.out.println("Writing");
            User user = userDao.getUser(userId).orElseThrow(() -> new WebApplicationException(""));

            System.out.println("userId = " + userId);

            Boolean result = faceRecognitionService.verifyImage(file, user.getModel());
            return new Authenticated(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in handling file.");
        }
    }

}
