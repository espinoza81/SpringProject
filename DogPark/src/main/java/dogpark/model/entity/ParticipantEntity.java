package dogpark.model.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "participants")
public class ParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private DogEntity dog;

    private boolean award;

    private int money;

    public ParticipantEntity(DogEntity dog) {
        this.dog = dog;
        this.award = false;
        this.money = 0;
    }
}
