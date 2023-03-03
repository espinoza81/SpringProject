package dogpark.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

  @GetMapping("/")
  public String home() {

    return "index";
  }

  @GetMapping("/users/register")
  public String register() {
    return "auth-register";
  }
}
