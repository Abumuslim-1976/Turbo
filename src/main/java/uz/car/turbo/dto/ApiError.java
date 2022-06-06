package uz.car.turbo.dto;

import lombok.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String message;
    private String path;
    private Integer status;
}
