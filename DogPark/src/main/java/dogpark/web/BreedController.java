package dogpark.web;

import dogpark.model.dtos.AddBreedDTO;
import dogpark.model.dtos.BreedInfoDTO;
import dogpark.service.BreedService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/breeds")
@Controller
public class BreedController {

    private final BreedService breedService;

    public BreedController(BreedService breedService) {
        this.breedService = breedService;
    }

    @ModelAttribute("addBreedDTO")
    public AddBreedDTO initAddOfferDTO(){
        return new AddBreedDTO();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public String addBreed() {
        return "add-breed";
    }

    @GetMapping("/all")
    public String allBreads(Model model) {
        List<BreedInfoDTO> allBreeds = this.breedService.getAllBreads();
        model.addAttribute("allBreeds", allBreeds);
        return "all-breeds";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public String addBreed(@Valid AddBreedDTO addBreedDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("addBreedDTO", addBreedDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addBreedDTO", bindingResult);

            return "redirect:/breeds/add";
        }

        this.breedService.created(addBreedDTO);

        return "redirect:/breeds/all";
    }
}
