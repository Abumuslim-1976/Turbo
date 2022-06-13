package uz.car.turbo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.car.turbo.dto.ApiResult;
import uz.car.turbo.dto.LoginDto;
import uz.car.turbo.dto.SignUpDto;
import uz.car.turbo.dto.TokenDto;
import uz.car.turbo.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody LoginDto loginDto) {
        ApiResult<TokenDto> tokenDtoApiResult = authService.signIn(loginDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(tokenDtoApiResult);
    }

    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto) {
        ApiResult<TokenDto> tokenDtoApiResult = authService.signUp(signUpDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(tokenDtoApiResult);
    }

}
