package uz.car.turbo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SessionDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long issuedAt;
    private Long expiresIn;
    private Long refreshTokenExpire;
}
