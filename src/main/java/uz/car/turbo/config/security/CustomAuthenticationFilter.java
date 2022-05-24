package uz.car.turbo.config.security;


import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.car.turbo.domain.User;
import uz.car.turbo.dto.ApiError;
import uz.car.turbo.dto.AuthUserDto;
import uz.car.turbo.dto.SessionDto;
import uz.car.turbo.utils.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            BufferedReader reader = request.getReader();
            String collect = reader.lines().collect(Collectors.joining());
            AuthUserDto authUser = new ObjectMapper().readValue(collect, AuthUserDto.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        String accessToken = JWT
                .create()
                .withSubject(user.getUsername())
                .withExpiresAt(JwtUtils.getExpireDateForAccessToken())
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(JwtUtils.getAlgorithm());

        String refreshToken = JWT
                .create()
                .withSubject(user.getUsername())
                .withExpiresAt(JwtUtils.getExpireDateForRefreshToken())
                .withIssuer(request.getRequestURL().toString())
                .sign(JwtUtils.getAlgorithm());

        SessionDto.SessionDtoBuilder sessionDto = SessionDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpire(JwtUtils.getExpireDateForRefreshToken().getTime())
                .issuedAt(System.currentTimeMillis())
                .expiresIn(JwtUtils.getExpireDateForRefreshToken().getTime());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), sessionDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ApiError.ApiErrorBuilder apiError = ApiError
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("NOT_FOUND")
                .path(request.getRequestURL().toString())
                .date(LocalDate.now());
        new ObjectMapper().writeValue(response.getOutputStream(), apiError);
    }
}
