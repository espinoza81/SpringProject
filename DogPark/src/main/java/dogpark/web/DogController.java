package dogpark.web;

import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.AddSaleStudDTO;
import dogpark.model.dtos.DogWithPriceDTO;
import dogpark.service.DogService;
import dogpark.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class DogController {
    private final DogService dogService;
    private final UserService userService;
    private static final int FOOD_HEALTH_INCREASE = 20;
    private static final int TREAT_HEALTH_INCREASE = 40;

    public DogController(DogService dogService,
                         UserService userService) {
        this.dogService = dogService;
        this.userService = userService;
    }

    @PreAuthorize("isOwner(#dogId)")
    @GetMapping("/dog/{id}")
    public String dog(@PathVariable("id") Long dogId,
                      Model model,
                      @AuthenticationPrincipal UserDetails user) {

        DogWithPriceDTO dog = dogService.getDogInfoById(dogId).
                orElseThrow(() -> new ObjectNotFoundException("Dog with ID "+ dogId + "not found"));

        int money = userService.getMoney(user.getUsername());

        model.addAttribute("dog", dog);
        model.addAttribute("money", money);

        return "dog";
    }

    @PreAuthorize("isOwner(#dogId)")
    @PostMapping("/dog/{id}/food")
    public String dogFood(@PathVariable("id") Long dogId,
                          @AuthenticationPrincipal UserDetails user){

        dogService.increaseDogHealth(dogId, FOOD_HEALTH_INCREASE);
        userService.decreaseLoggedUserMoney(user.getUsername(), "food");

        return "redirect:/dog/{id}";
    }

    @PreAuthorize("isOwner(#dogId)")
    @PostMapping("/dog/{id}/treat")
    public String dogGiveTreat(@PathVariable("id") Long dogId,
                          @AuthenticationPrincipal UserDetails user){

        dogService.increaseDogHealth(dogId, TREAT_HEALTH_INCREASE);
        userService.decreaseLoggedUserMoney(user.getUsername(), "treat");

        return "redirect:/dog/{id}";
    }

    @PreAuthorize("isOwner(#dogId)")
    @PostMapping("/dog/{id}/grooming")
    public String giveGrooming(@PathVariable("id") Long dogId,
                               @AuthenticationPrincipal UserDetails user){

        dogService.getGroomingProcedure(dogId);
        userService.decreaseLoggedUserMoney(user.getUsername(), "lesson");

        return "redirect:/dog/{id}";
    }

    @PreAuthorize("isOwner(#dogId)")
    @PostMapping("/dog/{id}/hunting")
    public String getHuntingLesson(@PathVariable("id") Long dogId,
                               @AuthenticationPrincipal UserDetails user){

        dogService.getHuntingLesson(dogId);
        userService.decreaseLoggedUserMoney(user.getUsername(), "lesson");

        return "redirect:/dog/{id}";
    }

    @PreAuthorize("isOwner(#dogId)")
    @PostMapping("/dog/{id}/agility")
    public String getAgilityLesson(@PathVariable("id") Long dogId,
                               @AuthenticationPrincipal UserDetails user){

        dogService.getAgilityLesson(dogId);
        userService.decreaseLoggedUserMoney(user.getUsername(), "lesson");

        return "redirect:/dog/{id}";
    }
}