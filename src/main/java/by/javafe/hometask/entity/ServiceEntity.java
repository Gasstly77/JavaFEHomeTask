package by.javafe.hometask.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "services", schema = "hometask")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_entity_seq_gen")
    @SequenceGenerator(
            name = "service_entity_seq_gen",
            sequenceName = "hometask.service_entity_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;
}
