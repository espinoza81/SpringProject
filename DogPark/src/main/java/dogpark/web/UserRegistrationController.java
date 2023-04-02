package dogpark.web;

import dogpark.model.dtos.UserRegistrationDTO;
import dogpark.model.enums.SexEnum;
import dogpark.service.BreedService;
import dogpark.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserRegistrationController {
    private final UserService userService;
    private final BreedService breedService;

    @Autowired
    public UserRegistrationController(UserService userService,
                                      BreedService breedService) {
        this.userService = userService;

        this.breedService = breedService;
    }

    @ModelAttribute("registrationDTO")
    public UserRegistrationDTO initUserRegistrationDTO(){
        return new UserRegistrationDTO();
    }
    @GetMapping("/users/register")
    public String register(Model model) {


        List<String> breeds = breedService.getAllBreadsName();


        model.addAttribute("sexM", SexEnum.M);
        model.addAttribute("sexF", SexEnum.F);
        model.addAttribute("breeds", breeds);

        return "auth-register";
    }

    @PostMapping("/users/register")
    public String register(
            @Valid UserRegistrationDTO registrationDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {


        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("registrationDTO", registrationDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.registrationDTO", bindingResult);

            return "redirect:/users/register";
        }

        userService.registerUser(registrationDTO);

        return "redirect:/users/login";
    }

}
