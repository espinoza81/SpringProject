package dogpark.model.entity;

import dogpark.model.enums.SexEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @OneToMany(fetch = FetchType.EAGER)
    private List<PartnerEntity> studOffers;

    @ManyToOne
    private UserEntity owner;
}
