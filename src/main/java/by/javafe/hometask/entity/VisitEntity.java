package by.javafe.hometask.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "visits", schema = "hometask")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visit_entity_seq_gen")
    @SequenceGenerator(
            name = "visit_entity_seq_gen",
            sequenceName = "hometask.visit_entity_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "amount_spent", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountSpent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitor_id", nullable = false)
    private VisitorEntity visitor;
}

