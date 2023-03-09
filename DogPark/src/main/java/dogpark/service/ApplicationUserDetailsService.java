package dogpark.service;

import dogpark.model.AppUserDetails;
import dogpark.model.entity.UserEntity;
import dogpark.model.entity.UserRoleEntity;
import dogpark.repository.UserRepository;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ApplicationUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public ApplicationUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return
        userRepository.
                findUserEntityByEmail(email).
            map(this::map).
            orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found!"));
  }

  private UserDetails map(UserEntity userEntity) {
    return new AppUserDetails(
            userEntity.getEmail(),
            userEntity.getPassword(),
            extractAuthorities(userEntity))
            .setMoney(userEntity.getMoney())
            .setGameUsername(userEntity.getGameUsername());
  }

  private List<GrantedAuthority> extractAuthorities(UserEntity userEntity) {
    return userEntity.
        getRoles().
        stream().
        map(this::mapRole).
        toList();
  }

  private GrantedAuthority mapRole(UserRoleEntity userRoleEntity) {
    return new SimpleGrantedAuthority("ROLE_" + userRoleEntity.getRole().name());
  }
}
