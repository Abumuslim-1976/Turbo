package uz.car.turbo.dto;

import lombok.*;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionDto {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private Long refreshTokenExpire;
    private Long expiresIn;
    private Long issuedAt;
}
