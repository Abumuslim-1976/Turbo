package uz.car.turbo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.hibernate.Hibernate;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NormalizerDef;
import org.hibernate.search.annotations.TokenFilterDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "brand"})})
@Indexed
@NormalizerDef(name = "lower", filters = @TokenFilterDef(factory = LowerCaseFilterFactory.class))
public class Car extends AbstractEntity<Long> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer rentPrice;

    @Column(nullable = false)
    private String brand;

    private String colour;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Car car = (Car) o;
        return getId() != null && Objects.equals(getId(), car.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
