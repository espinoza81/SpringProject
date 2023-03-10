package dogpark.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, name = "shelter_name")
    private String shelterName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true, name = "username")
    private String gameUsername;
    @Column(nullable = false)
    private String password;

    private int money;

    @OneToMany(mappedBy = "owner")
    private List<DogEntity> dogs;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<UserRoleEntity> roles;

    public UserEntity() {
        this.money = 1000;
        this.dogs = new ArrayList<>();
        this.roles = new ArrayList<>();
    }


    public UserEntity addRole(UserRoleEntity role) {
        this.roles.add(role);
        return this;
    }

    public UserEntity addDog(DogEntity dog) {
        this.dogs.add(dog);
        return this;
    }

    public UserEntity addMoney(int money) {
        this.money += money;
        return this;
    }

    public UserEntity spendMoney(int money) {
        this.money -= money;
        return this;
    }


    @Override
    public String toString() {
        return "UserEntity{" +
                "email='" + shelterName + '\'' +
                ", username='" + email + '\'' +
                '}';
    }
}
