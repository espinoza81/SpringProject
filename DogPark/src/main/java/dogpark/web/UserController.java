package dogpark.web;

import dogpark.model.dtos.ShelterDTO;
import dogpark.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/")
  public String home() {

    return "index";
  }

  @GetMapping("/users/shelter")
  public String shelter(Model model,
                        @AuthenticationPrincipal UserDetails user) {

    ShelterDTO shelter = userService.getShelterStats(user.getUsername());

    model.addAttribute("shelter", shelter);

    return "shelter";
  }

  @GetMapping("/users/dog")
  public String dogs() {
    return "my_dogs";
  }
}
