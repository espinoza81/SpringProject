package dogpark.service;

import dogpark.model.entity.UserEntity;
import dogpark.model.entity.UserRoleEntity;
import dogpark.model.enums.UserRoleEnum;
import dogpark.repository.UserRepository;
import dogpark.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InitService {

  private final UserRoleRepository userRoleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final String defaultPassword;

  public InitService(UserRoleRepository userRoleRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      @Value("${app.default.password}") String defaultPassword) {
    this.userRoleRepository = userRoleRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.defaultPassword = defaultPassword;
  }

  @PostConstruct
  public void init() {
    initRoles();
    initUsers();
  }

  private void initRoles() {
    if (userRoleRepository.count() == 0) {
      var adminRole = new UserRoleEntity().setRole(UserRoleEnum.ADMIN);

      userRoleRepository.save(adminRole);
    }
  }

  private void initUsers() {
    if (userRepository.count() == 0) {
      initAdmin();
      initNormalUser();
    }
  }

  private void initAdmin(){
    var adminUser = UserEntity.builder().
            shelterName("AdminDogs").
            email("admin@example.com").
            gameUsername("Admin").
            password(passwordEncoder.encode(defaultPassword)).
            roles(userRoleRepository.findAll()).
            build();

    userRepository.save(adminUser);
  }

  private void initNormalUser(){

    var normalUser = UserEntity.builder().
            shelterName("EspinozaDogs").
            email("espinoza81@yahoo.com").
            gameUsername("Espinoza").
            money(1000).
            password(passwordEncoder.encode(defaultPassword)).
            build();

    userRepository.save(normalUser);
  }
}
