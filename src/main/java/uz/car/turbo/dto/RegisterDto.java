package uz.car.turbo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String accessToken;
    private String refreshToken;
    private  boolean registered;
}
