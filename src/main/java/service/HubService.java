package service;

import dao.*;
import dto.*;
import dto.response.UserDetailed;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HubService {

    private UserDao userDao;
    private QuestionDao questionDao;
    private ExaminationDao examinationDao;
    private RegistrationDao registrationDao;
    private ResponseDao responseDao;
    private static final Logger logger = LoggerFactory.getLogger(HubService.class);

    public HubService(
            UserDao userDao,
            QuestionDao questionDao,
            ExaminationDao examinationDao,
            RegistrationDao registrationDao,
            ResponseDao responseDao) {
        this.userDao = userDao;
        this.questionDao = questionDao;
        this.examinationDao = examinationDao;
        this.registrationDao = registrationDao;
        this.responseDao = responseDao;
    }

    /**
     * return question paper.
     */
    public Questions getQuestions(ObjectId testId) {
        Test test = examinationDao.getTest(testId).orElseThrow(() -> new WebApplicationException("Invalid test id."));
        logger.info("loading questions from database.");
        return questionDao.getQuestionSet(testId).get();
    }

    /**
     * get response.
     */
    public void saveResponse(List<Responses> responsesList) {
        responsesList.forEach(responses ->
                responseDao.saveResponse(responses));
    }

    /**
     * return details of all registered users.
     */
    public List<UserDetailed> getUserDetailsForTest(ObjectId testId) {
        Test test = examinationDao.getTest(testId).orElseThrow(() -> new WebApplicationException("Invalid test id."));
        List<Registration> registrations = registrationDao.getAllRegistrationForTest(testId);
        Set<ObjectId> userIds = registrations.stream().map(Registration::getUserId).collect(Collectors.toSet());
        List<User> registeredUsers = userDao.getSpecificUsers(userIds);
        HashMap<ObjectId, User> userIdMap = new HashMap<>();
        registeredUsers.forEach(candidate -> userIdMap.put(candidate.getId(), candidate));
        List<UserDetailed> candidateResource = registrations.stream().map(registrationEntry -> {
            User user = userIdMap.get(registrationEntry.getUserId());
            return new UserDetailed(
                    registrationEntry.getId(),
                    user.getName(),
                    registrationEntry.getRegistration(),
                    user.getPhone(),
                    user.getPicture()
            );
        }).collect(Collectors.toList());
        return candidateResource;
    }
}
