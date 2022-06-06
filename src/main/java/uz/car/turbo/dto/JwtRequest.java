package uz.car.turbo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest implements Serializable {

    @ApiModelProperty(example = "admin")
    private String username;
    @ApiModelProperty(example = "1122")
    private String password;

}
