package dogpark.model.entity;

import dogpark.model.enums.UserRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "roles")
public class UserRoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRoleEnum role;


  public Long getId() {
    return id;
  }

  public UserRoleEntity setId(Long id) {
    this.id = id;
    return this;
  }

  public UserRoleEnum getRole() {
    return role;
  }

  public UserRoleEntity setRole(UserRoleEnum role) {
    this.role = role;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserRoleEntity that = (UserRoleEntity) o;
    return id.equals(that.id) && role == that.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, role);
  }

  @Override
  public String toString() {
    return "UserRoleEntity{" +
        "id=" + id +
        ", role=" + role +
        '}';
  }
}
