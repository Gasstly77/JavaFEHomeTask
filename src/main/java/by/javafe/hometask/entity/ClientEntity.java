package by.javafe.hometask.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "clients", schema = "hometask")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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

    @Column(name = "year_of_birth")
    private Integer yearOfBirth;

    @Embedded
    private Address address;
}