package dogpark.model.entity;

import dogpark.model.enums.SexEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "dogs")
public class DogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SexEnum sex;

    @ManyToOne
    @JoinColumn(name = "breed_id")
    private BreedEntity breedEntity;

    private String name;

    private int grooming;

    private int hunting;

    private int agility;
    private int health;

    private int awardCup;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private SaleEntity sale;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PartnerEntity> studOffers;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserEntity owner;

    public void addStudOffer(PartnerEntity studOffer){
        this.studOffers.add(studOffer);
    }

    public DogEntity addAward() {
        this.awardCup ++;
        return this;
    }

    public DogEntity addMoneyToOwner(int money){
        this.getOwner().addMoney(money);
        return this;
    }
}
