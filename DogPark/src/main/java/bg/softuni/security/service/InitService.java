package bg.softuni.security.service;

import bg.softuni.security.model.entity.UserEntity;
import bg.softuni.security.model.entity.UserRoleEntity;
import bg.softuni.security.model.enums.UserRoleEnum;
import bg.softuni.security.repository.UserRepository;
import bg.softuni.security.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InitService {

  private final UserRoleRepository userRoleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public InitService(UserRoleRepository userRoleRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      @Value("${app.default.password}") String defaultPassword) {
    this.userRoleRepository = userRoleRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
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
    var adminUser = new UserEntity().
            setShelterName("AdminDogs").
            setEmail("admin@example.com").
            setUsername("Admin").
            setPassword(passwordEncoder.encode("qazwsx")).
            setRoles(userRoleRepository.findAll());

    userRepository.save(adminUser);
  }

  private void initNormalUser(){

    var normalUser = new UserEntity().
            setShelterName("EspinozaDogs").
            setEmail("espinoza81@yahoo.com").
            setUsername("Espinoza").
            setPassword(passwordEncoder.encode("qazwsx"));

    userRepository.save(normalUser);
  }
}
