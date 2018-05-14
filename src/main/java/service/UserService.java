package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDao;
import dto.JwtPayload;
import dto.Test;
import dto.User;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserService {

    private UserDao userDao;
    private transient final String secret;
    private transient final static long EXPIRY_MILLIS = 60 * 60 * 1000;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserDao userDao, String secret) {
        this.userDao = userDao;
        this.secret = secret;
    }

    public SignInResponse signup(String nameFirst, String nameLast, String email, String phone, String password) {
        userDao.getUserByPhone(phone)
                .ifPresent(d -> {
                    throw new WebApplicationException("User already registered.", Response.Status.BAD_REQUEST);
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
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    jwtToken,
                    expiryTime.getTime()
            );
        } else {
            throw new WebApplicationException();
        }
    }

    public SignInResponse signin(String phone, String password) {
        final User user = userDao.getUserByPhone(phone)
                .orElseThrow(() -> new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED));
        if (!authenticate(password, user.getPassword())) {
            throw new WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED);
        }

        final Date expiryTime = new Date(System.currentTimeMillis() + EXPIRY_MILLIS);
        final JwtPayload jwtPayload = new JwtPayload(user.getId());
        final String jwtToken = createToken(jwtPayload, expiryTime, secret);

        return new SignInResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                jwtToken,
                expiryTime.getTime()
        );
    }

    public List<Test> listExam() {
        int count = 4;
        for (int i = 0; i < count; ++i) {
            Test test = new Test(
                    new ObjectId(),
                    "Test",
                    new Date()
            );
            allTests.add(test);
        }
        return allTests;
    }
    private List<Test> allTests = new ArrayList<>();

    public void registerExam(ObjectId userId, ObjectId test) {

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
