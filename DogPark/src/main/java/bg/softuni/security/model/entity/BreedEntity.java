package bg.softuni.security.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "breeds")
public class BreedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int grooming;

    private int hunting;

    private int agility;

}
