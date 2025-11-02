package by.javafe.hometask.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Setter
@ToString
@Subselect("SELECT r.id, r.name, r.hourly_rate FROM rooms r WHERE r.max_capacity <= 15")
@Synchronize("rooms")
public class SmallRoomView {
    @Id
    private Long id;
    private String name;
    private Long hourlyRate;
}

