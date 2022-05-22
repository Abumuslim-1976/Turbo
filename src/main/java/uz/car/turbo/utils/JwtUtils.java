package uz.car.turbo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtils {

    public static int expire = 6000000;
    public static String secret = "Abumuslim1998Amonov98Bahriddinovich";

    public static Date getExpireDateForAccesToken() {
        return new Date(expire + System.currentTimeMillis());
    }

    public static Date getExpireDateForRefreshToken() {
        return new Date(expire + System.currentTimeMillis() + 3600 * 60 * 3600);
    }

    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
    }

    public static JWTVerifier getVerifier() {
        return JWT.require(getAlgorithm()).build();
    }

}
