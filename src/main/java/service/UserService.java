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
import dto.response.SignInResponse;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.apache.commons.codec.binary.Base64;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.*;
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
    private transient final static long EXPIRY_MILLIS = 60 * 60 * 1000;
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

    private String encodeFileToBase64Binary(File file)
            throws IOException {
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encodeBase64(bytes);
        return new String(encoded);
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }


    public static byte[] getByts(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }

    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
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
