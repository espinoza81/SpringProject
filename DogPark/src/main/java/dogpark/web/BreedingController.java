package dogpark.web;

import dogpark.model.dtos.DogDTO;
import dogpark.model.dtos.SaleStudDTO;
import dogpark.service.BreedingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BreedingController {
    private final BreedingService breedingService;

    public BreedingController(BreedingService breedingService) {
        this.breedingService = breedingService;
    }

    @GetMapping("/market/stud")
    public String dogsForStud(Model model) {

        List<SaleStudDTO> dogsForStud = breedingService.getDogsForStud();

        model.addAttribute("dogsForStud", dogsForStud);

        return "stud_offers";
    }
}
