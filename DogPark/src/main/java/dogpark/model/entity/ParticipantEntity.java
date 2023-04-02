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

    private int score;
    private boolean active;

    public ParticipantEntity(DogEntity dog) {
        this.dog = dog;
        this.award = false;
        this.money = 0;
        this.score = 0;
        this.active = true;
    }

    public ParticipantEntity setAward(boolean award) {
        this.award = award;
        return this;
    }

    public ParticipantEntity setMoney(int money) {
        this.money = money;
        return this;
    }

    public ParticipantEntity setActive(boolean active) {
        this.active = active;
        return this;
    }
}
