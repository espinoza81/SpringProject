package dogpark.web;

import dogpark.model.dtos.AddSaleStudDTO;
import dogpark.model.dtos.DogWithNameIdDTO;
import dogpark.model.dtos.SaleStudDTO;
import dogpark.service.BreedingService;
import dogpark.service.DogService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BreedingController {
    private final BreedingService breedingService;
    private final DogService dogService;

    public BreedingController(BreedingService breedingService,
                              DogService dogService) {
        this.breedingService = breedingService;
        this.dogService = dogService;
    }

    @ModelAttribute("addStudDTO")
    public AddSaleStudDTO initAddStudDTO(){
        return new AddSaleStudDTO();
    }

    @GetMapping("/market/stud")
    public String dogsForStud(Model model,
                              @AuthenticationPrincipal UserDetails user) {

        List<SaleStudDTO> studOffers = breedingService.getDogsForStud();
        List<DogWithNameIdDTO> dogsOwnerForStud = this.dogService.getDogsSexM(user.getUsername());

        System.out.println();

        model.addAttribute("studOffers", studOffers);
        model.addAttribute("dogsOwner", dogsOwnerForStud);

        return "stud_offers";
    }

    @PreAuthorize("isOwner(#addStudDTO.dogId)")
    @PostMapping("/dog/stud")
    public String studSingleDog(@Valid AddSaleStudDTO addStudDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("addStudDTO", addStudDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addStudDTO", bindingResult);

            return "redirect:/market/stud";
        }

        this.dogService.createdStud(addStudDTO);

        return "redirect:/market/stud";
    }
}
