package dogpark.web;

import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.DogWithPriceDTO;
import dogpark.service.DogService;
import dogpark.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DogController {
    private final DogService dogService;
    private final UserService userService;

    public DogController(DogService dogService,
                         UserService userService) {
        this.dogService = dogService;
        this.userService = userService;
    }

    @PreAuthorize("@dogService.isOwner(#userDetails.username, #dogId) && @dogService.isNotForSale(#dogId)")
    @GetMapping("/dog/{id}")
    public String dog(@PathVariable("id") Long dogId,
                      @AuthenticationPrincipal UserDetails userDetails,
                      Model model) {

        DogWithPriceDTO dog = dogService.getDogInfoById(dogId).
                orElseThrow(() -> new ObjectNotFoundException("Dog with ID "+ dogId + "not found"));

        int money = userService.getMoney(userDetails.getUsername());

        model.addAttribute("dog", dog);
        model.addAttribute("money", money);

        return "dog";
    }

    @PreAuthorize("@dogService.isOwner(#userDetails.username, #dogId) && @dogService.isNotForSale(#dogId)")
    @PostMapping("/dog/{id}/food")
    public String dogFood(@PathVariable("id") Long dogId,
                          @AuthenticationPrincipal UserDetails userDetails){

        dogService.giveFood(dogId);

        return "redirect:/dog/{id}";
    }

    @PreAuthorize("@dogService.isOwner(#userDetails.username, #dogId) && @dogService.isNotForSale(#dogId)")
    @PostMapping("/dog/{id}/treat")
    public String dogGiveTreat(@PathVariable("id") Long dogId,
                               @AuthenticationPrincipal UserDetails userDetails){

        dogService.giveTreat(dogId);

        return "redirect:/dog/{id}";
    }

    @PreAuthorize("@dogService.isOwner(#userDetails.username, #dogId) && @dogService.isNotForSale(#dogId)")
    @PostMapping("/dog/{id}/grooming")
    public String giveGrooming(@PathVariable("id") Long dogId,
            @AuthenticationPrincipal UserDetails userDetails){

        dogService.getGroomingProcedure(dogId);

        return "redirect:/dog/{id}";
    }

    @PreAuthorize("@dogService.isOwner(#userDetails.username, #dogId) && @dogService.isNotForSale(#dogId)")
    @PostMapping("/dog/{id}/hunting")
    public String getHuntingLesson(@PathVariable("id") Long dogId,
                                   @AuthenticationPrincipal UserDetails userDetails){

        dogService.getHuntingLesson(dogId);

        return "redirect:/dog/{id}";
    }

    @PreAuthorize("@dogService.isOwner(#userDetails.username, #dogId) && @dogService.isNotForSale(#dogId)")
    @PostMapping("/dog/{id}/agility")
    public String getAgilityLesson(@PathVariable("id") Long dogId,
                                   @AuthenticationPrincipal UserDetails userDetails){

        dogService.getAgilityLesson(dogId);

        return "redirect:/dog/{id}";
    }
}