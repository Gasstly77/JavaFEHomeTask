package by.javafe.hometask.entity;

import by.javafe.hometask.constant.ClientStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "visitors", schema = "hometask")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VisitorEntity extends ClientEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClientStatus status;

    @Column(name = "last_visit_date")
    private LocalDate lastVisitDate;

    @Column(name = "total_spent", nullable = false)
    private Double totalSpent;

    @Column(name = "first_visit_date")
    private LocalDate firstVisitDate;
}

