package by.javafe.hometask.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "services", schema = "hometask")
@Data
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;
}
