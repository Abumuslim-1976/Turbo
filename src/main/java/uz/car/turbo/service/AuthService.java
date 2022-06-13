package uz.car.turbo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import uz.car.turbo.dto.ApiResult;
import uz.car.turbo.dto.LoginDto;
import uz.car.turbo.dto.SignUpDto;
import uz.car.turbo.dto.TokenDto;

import java.util.UUID;

public interface AuthService extends UserDetailsService {

    UserDetails loadUserById(UUID id);

    ApiResult<TokenDto> signIn(LoginDto loginDto);

    ApiResult<TokenDto> signUp(SignUpDto signUpDto);
}
