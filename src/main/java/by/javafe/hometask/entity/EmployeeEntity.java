package by.javafe.hometask.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees", schema = "hometask")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployeeEntity extends ClientEntity {

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "dismissal_date")
    private LocalDate dismissalDate;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "monthly_salary", nullable = false)
    private BigDecimal monthlySalary;
}

