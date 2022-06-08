package uz.car.turbo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import uz.car.turbo.config.swagger.ApiProperties;
import uz.car.turbo.dto.ApiError;
import uz.car.turbo.dto.AuthUserDto;
import uz.car.turbo.dto.Data;
import uz.car.turbo.dto.SessionDto;
import uz.car.turbo.repository.UserRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final ApiProperties properties;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public ResponseEntity<Data<SessionDto>> login(AuthUserDto dto, WebRequest request) {
        try {
            HttpClient httpclient = HttpClientBuilder.create().build();
            System.out.println(properties.getRequest() + properties.getApi());
            HttpPost httppost = new HttpPost(properties.getRequest() + properties.getApi() + "/api/login");

            byte[] bytes = objectMapper.writeValueAsBytes(dto);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httppost.setEntity(new InputStreamEntity(byteArrayInputStream));

            HttpResponse response = httpclient.execute(httppost);
            JsonNode json_auth = objectMapper.readTree(EntityUtils.toString(response.getEntity()));
            JsonNode apiError = json_auth.get("apiError");

            if (apiError == null) {
                JsonNode node = json_auth.get("body");
                SessionDto sessionDto = objectMapper.readValue(node.toString(), SessionDto.class);
                return new ResponseEntity<>(new Data<>(sessionDto), HttpStatus.OK);
            }
            return new ResponseEntity<>(new Data<>(ApiError.builder()
                    .message("bad request")
                    .path(request.getContextPath())
                    .status(HttpStatus.BAD_REQUEST.value()).build()), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new Data<>(ApiError.builder()
                    .message("bad request")
                    .path(request.getContextPath())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()), HttpStatus.OK);
        }
    }

}
