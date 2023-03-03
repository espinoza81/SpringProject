package dogpark.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DogController {

    @GetMapping("/users/dog")
    public String register() {
        return "my_dogs";
    }
}
