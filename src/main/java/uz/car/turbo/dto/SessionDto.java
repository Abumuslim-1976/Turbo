package uz.car.turbo.dto;

import lombok.*;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionDto {
    private String sessionToken;
    private String tokenType;
    private String refreshToken;
    private Long expiresIn;
    private String scope;
}
