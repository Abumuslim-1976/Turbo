package uz.car.turbo.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uz.car.turbo.exception.RestException;
import uz.car.turbo.exception.TokenExpiredException;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    @Value("${accessTokenLifeTime}")
    private String accessTokenLifeTime;

    @Value("${refreshTokenLifeTime}")
    private String refreshTokenLifeTime;

    @Value("${secretKey}")
    private String secretKey;


    public String generateToken(UUID id, boolean isAccess) {
        Date expiredDate = new Date(System.currentTimeMillis() + (isAccess ? accessTokenLifeTime : refreshTokenLifeTime));
        return "Bearer " + Jwts
                .builder()
                .setSubject(id.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getIdFromToken(String token){
        try {
            return Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException();
        } catch (Exception e) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    public void validateToken(String token) {
        Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);
    }

}
