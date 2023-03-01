package bg.softuni.security.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
  @Column(nullable = false, unique = true)
  private String username;
  @Column(nullable = false)
  private String password;

  private int food;
  private int treat;

  private int money;

  @OneToMany
  private List<DogEntity> dogs;

  @OneToMany
  private List<SaleEntity> sales;

  @OneToMany
  private List<PartnerEntity> breedingOffers;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<UserRoleEntity> roles = new ArrayList<>();

  public UserEntity() {
    this.money = 1000;
    this.dogs = new ArrayList<>();
    this.sales = new ArrayList<>();
    this.breedingOffers = new ArrayList<>();
    this.roles = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public UserEntity setId(Long id) {
    this.id = id;
    return this;
  }

  public String getShelterName() {
    return shelterName;
  }

  public UserEntity setShelterName(String email) {
    this.shelterName = email;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public UserEntity setPassword(String password) {
    this.password = password;
    return this;
  }

  public List<UserRoleEntity> getRoles() {
    return roles;
  }

  public UserEntity setRoles(List<UserRoleEntity> roles) {
    this.roles = roles;
    return this;
  }

  public UserEntity addRole(UserRoleEntity role) {
    this.roles.add(role);
    return this;
  }

  public UserEntity addDog(DogEntity dog) {
    this.dogs.add(dog);
    return this;
  }

  public UserEntity addSales(SaleEntity sale) {
    this.sales.add(sale);
    return this;
  }

  public UserEntity addPartnerOffer(PartnerEntity partnerEntity) {
    this.breedingOffers.add(partnerEntity);
    return this;
  }

  public String getEmail() {
    return email;
  }

  public UserEntity setEmail(String username) {
    this.email = username;
    return this;
  }

  public UserEntity setFood(int food) {
    this.food = food;
    return this;
  }

  public UserEntity setTreat(int treat) {
    this.treat = treat;
    return this;
  }

  public UserEntity setMoney(int money) {
    this.money = money;
    return this;
  }

  public UserEntity setDogs(List<DogEntity> dogs) {
    this.dogs = dogs;
    return this;
  }

  public UserEntity setSales(List<SaleEntity> sales) {
    this.sales = sales;
    return this;
  }

  public UserEntity setBreedingOffers(List<PartnerEntity> breedingOffers) {
    this.breedingOffers = breedingOffers;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public UserEntity setUsername(String username) {
    this.username = username;
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
