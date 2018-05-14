package auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDao;
import dto.JwtPayload;
import dto.User;
import io.dropwizard.auth.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by gkvm on 6/4/17.
 */
public class Authenticator implements io.dropwizard.auth.Authenticator<String, User> {

    private final String secret;
    private final UserDao userDao;

    public Authenticator(String secret, UserDao userDao) {
        this.secret = secret;
        this.userDao = userDao;
    }

    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(TextCodec.BASE64.decode(secret))
                    .parseClaimsJws(token)
                    .getBody();
            try {
                final ObjectMapper mapper = new ObjectMapper();
                final JwtPayload jwtPayload = mapper.readValue(claims.getSubject(), JwtPayload.class);

                return userDao.getUser(jwtPayload.getUserId());
            } catch (IOException e) {
                throw new AuthenticationException("Token parse error.", e);
            }
        } catch (JwtException e) {
            return Optional.empty();
        }
    }
}
