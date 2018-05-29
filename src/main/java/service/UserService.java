package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ExaminationDao;
import dao.RegistrationDao;
import dao.UserDao;
import dto.JwtPayload;
import dto.Registration;
import dto.Test;
import dto.User;
import dto.response.LoginToExam;
import dto.response.SignInResponse;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {

    private UserDao userDao;
    private ExaminationDao examinationDao;
    private RegistrationDao registrationDao;
    private FaceRecognitionService faceRecognitionService;
    private transient final String secret;
    private transient final static long EXPIRY_MILLIS = 10 * 60 * 60 * 1000;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final String API_SERVER = "http://localhost:5001";

    public UserService(
            UserDao userDao,
            ExaminationDao examinationDao,
            RegistrationDao registrationDao,
            FaceRecognitionService faceRecognitionService,
            String secret) {
        this.userDao = userDao;
        this.examinationDao = examinationDao;
        this.registrationDao = registrationDao;
        this.faceRecognitionService = faceRecognitionService;
        this.secret = secret;
    }

    public SignInResponse signup(String nameFirst, String nameLast, String email, String phone, String password) {
        userDao.getUserByPhone(phone)
                .ifPresent(d -> {
                    throw new WebApplicationException("Phone number already registered.", Response.Status.BAD_REQUEST);
                });

        if (email != null)
            userDao.getUserByEmail(email)
                    .ifPresent(d -> {
                        throw new WebApplicationException("The give email id is already registered.", Response.Status.BAD_REQUEST);
                    });

        User user = new User(
                new ObjectId(),
                nameFirst,
                nameLast,
                email,
                phone,
                "",
                hashPassword(password)
        );

        Optional<ObjectId> userIdOptional = userDao.createUser(user);
        if (userIdOptional.isPresent()) {
            //Show response with the id.
            final Date expiryTime = new Date(System.currentTimeMillis() + EXPIRY_MILLIS);
            final JwtPayload jwtPayload = new JwtPayload(user.getId());
            final String jwtToken = createToken(jwtPayload, expiryTime, secret);

            return new SignInResponse(
                    user.getId(),
                    user.getCompleteName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getPicture(),
                    jwtToken,
                    expiryTime.getTime()
            );
        } else {
            throw new WebApplicationException();
        }
    }

    public SignInResponse signin(String phoneOrEmail, String password) {
        final User user = userDao.getUserByPhoneOrEmail(phoneOrEmail, phoneOrEmail)
                .orElseThrow(() -> new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED));
        if (!authenticate(password, user.getPassword())) {
            throw new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED);
        }

        final Date expiryTime = new Date(System.currentTimeMillis() + EXPIRY_MILLIS);
        final JwtPayload jwtPayload = new JwtPayload(user.getId());
        final String jwtToken = createToken(jwtPayload, expiryTime, secret);

        return new SignInResponse(
                user.getId(),
                user.getCompleteName(),
                user.getEmail(),
                user.getPhone(),
                user.getPicture(),
                jwtToken,
                expiryTime.getTime()
        );
    }

    public List<Test> listExam() {
        List<Test> examList = examinationDao.listExams();
        return examList;
    }

    public List<Test> listExamForUser(User user) {
        List<Test> examList = examinationDao.listExams();
        List<Registration> registrationsOfUser = registrationDao
                .listRegistrations(user.getId());
        Set<ObjectId> registeredIds = registrationsOfUser.stream()
                .map(Registration::getTestId)
                .collect(Collectors.toSet());
        return examList.stream().peek(exam -> {
            if (registeredIds.contains(exam.getId())) {
                exam.setHasApplied(true);
            }
        }).collect(Collectors.toList());
    }

    public void registerExam(User user, ObjectId testId) {
        Test test = examinationDao.getTest(testId).orElseThrow(() -> new WebApplicationException("Test not found."));
        registrationDao.checkIfRegistrationExists(user.getId(), test.getId());
        Registration registration = new Registration(new ObjectId(),
                test.getId(),
                user.getId(),
                getRandomNumber().toString(),
                user.getPhone()
        );
        registrationDao.createEntry(registration);
    }

    public void saveImage(User user, File file) {
        System.out.println("Writing");
        try {
            userDao.savePhoto(user.getId(), file.getCanonicalPath());
            List<Double> model = faceRecognitionService.trainImage(file);
            userDao.saveModel(user.getId(), model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LoginToExam mockLogin(User user, String registration, String password) {

        if(!(registration.equals("mock") && password.equals("2000"))){
            throw new WebApplicationException("Invalid credentials. Use the given credentials for mock exam", Response.Status.UNAUTHORIZED);
        }

//        Registration registrationInfo = registrationDao.getRegistration(registration)
//                .orElseThrow(() -> new WebApplicationException("Invalid credentials", javax.ws.rs.core.Response.Status.UNAUTHORIZED));
//        if (!password.equals(registrationInfo.getPassword())) {
//            throw new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED);
//        }

        return new LoginToExam(
                user.getId(),
                new ObjectId("5b0a3770fcb6b05c2d3fa058"),
                new ObjectId("5b0a3959fcb6b05c2d3fa0d7"),
                user.getCompleteName(),
                user.getEmail(),
                user.getPhone(),
                "",
                "Mock examination"
        );
    }

    private static Integer getRandomNumber() {
        Integer min = 1000000;
        Integer max = 9999999;
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }

    private static boolean authenticate(String password_plaintext, String stored_hash) {
        if (null == stored_hash || !stored_hash.startsWith("$2a$")) {
            logger.error("Invalid hash provided for comparison");
            return false;
        }
        return BCrypt.checkpw(password_plaintext, stored_hash);
    }

    private static String hashPassword(String password_plaintext) {
        return BCrypt.hashpw(password_plaintext, BCrypt.gensalt(12));
    }

    private static String createToken(final JwtPayload jwtPayload, final Date expiryTime, final String secret) {
        try {
            final JwtBuilder builder = Jwts.builder()
                    .setSubject(new ObjectMapper().writeValueAsString(jwtPayload))
                    .setExpiration(expiryTime)
                    .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.decode(secret));
            return builder.compact();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new WebApplicationException("JSON parse error.");
        }
    }
}
