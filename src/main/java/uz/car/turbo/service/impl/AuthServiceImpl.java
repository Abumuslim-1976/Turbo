package uz.car.turbo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.car.turbo.domain.User;
import uz.car.turbo.dto.ApiResult;
import uz.car.turbo.dto.LoginDto;
import uz.car.turbo.dto.SignUpDto;
import uz.car.turbo.dto.TokenDto;
import uz.car.turbo.exception.RestException;
import uz.car.turbo.jwt.JwtProvider;
import uz.car.turbo.repository.UserRepository;
import uz.car.turbo.service.AuthService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "user not found"));
    }

    @Override
    public UserDetails loadUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "user not found"));
    }

    @Override
    public ApiResult<TokenDto> signIn(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
            ));
            User user = (User) authenticate.getPrincipal();
            String accessToken = jwtProvider.generateToken(user.getId(), true);
            String refreshToken = jwtProvider.generateToken(user.getId(), false);
            return ApiResult.successResponse(new TokenDto(accessToken, refreshToken));
        } catch (Exception exception) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "Password or Username wrong");
        }

    }

    @Override
    public ApiResult<TokenDto> signUp(SignUpDto signUpDto) {
        User user = new User(
                signUpDto.getFirstName(),
                signUpDto.getLastName(),
                signUpDto.getPhoneNumber()
        );
        userRepository.save(user);
        String accessToken = jwtProvider.generateToken(user.getId(), true);
        String refreshToken = jwtProvider.generateToken(user.getId(), false);
        return ApiResult.successResponse(new TokenDto(accessToken, refreshToken));
    }
}











