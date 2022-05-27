package uz.car.turbo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Rentals extends AbstractEntity<Long> {

    @Column(nullable = false)
    private LocalDate from;

    @Column(nullable = false)
    private LocalDate to;

    private Integer duration;

    private Integer range;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Rentals rentals = (Rentals) o;
        return getId() != null && Objects.equals(getId(), rentals.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
