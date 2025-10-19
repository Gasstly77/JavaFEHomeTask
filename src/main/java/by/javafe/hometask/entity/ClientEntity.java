package by.javafe.hometask.entity;

import by.javafe.hometask.constant.ClientStatus;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Entity
@Table(name = "clients", schema = "hometask")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_entity_seq_gen")
    @SequenceGenerator(
            name = "client_entity_seq_gen",
            sequenceName = "hometask.client_entity_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "last_visit_date")
    private LocalDate lastVisitDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClientStatus status;

    @Column(name = "total_spent", nullable = false)
    private Double totalSpent;

    @Embedded
    private Address address;

    @Column(name = "premium")
    private boolean premium;
}