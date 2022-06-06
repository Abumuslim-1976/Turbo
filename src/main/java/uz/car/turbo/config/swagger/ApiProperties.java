package uz.car.turbo.config.swagger;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(value = "path")
public class ApiProperties {
    private String request;
    private String api;
    private String urlPath;
    private String filePath;
}
