package dogpark.web;

import dogpark.model.dtos.DogDTO;
import dogpark.service.DogService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MarketController {

    private final DogService dogService;

    public MarketController(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping("/market/sale")
    public String dogsForSale(Model model,
                       @AuthenticationPrincipal UserDetails user) {

        List<DogDTO> dogsForSale = dogService.getDogsForSale(user.getUsername());

        model.addAttribute("dogsForSale", dogsForSale);

        return "sale_offers";
    }

    @GetMapping("/market/stud")
    public String dogsForStud(Model model,
                       @AuthenticationPrincipal UserDetails user) {

        List<DogDTO> dogsForStud = dogService.getDogsForStud(user.getUsername());

        model.addAttribute("dogsForStud", dogsForStud);

        return "stud_offers";
    }
}
