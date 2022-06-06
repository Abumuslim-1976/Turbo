package uz.car.turbo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Data<D> implements Serializable {

    private D body;
    private ApiError apiError;
    private boolean success;

    public Data(D body) {
        this.success=true;
        this.body = body;
    }
    public Data(D body, HttpStatus status) {
        this.success=true;
        this.body = body;
    }

    public Data(ApiError apiError) {
        this.apiError = apiError;
        this.success=false;
    }

}
