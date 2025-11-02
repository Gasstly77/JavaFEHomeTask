package by.javafe.hometask.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@ToString
@Table(name = "clients")
@Where(clause = "premium = true")
public class PremiumClientEntity {
    @Id
    private Long id;
    private String name;
    private Boolean isPremium;
}
