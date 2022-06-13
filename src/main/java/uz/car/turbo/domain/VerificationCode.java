package uz.car.turbo.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode extends AbstractEntity<Long> {

    private String phoneNumber;

    private String code;

    private boolean confirmed;

}
