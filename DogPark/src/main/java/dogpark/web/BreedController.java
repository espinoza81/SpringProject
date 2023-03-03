package dogpark.web;

import dogpark.model.dtos.AddBreedDTO;
import dogpark.service.BreedService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/add")
    public String addBreed() {
        return "add-breed";
    }

    @GetMapping("/all")
    public String register() {
        return "all-breeds";
    }


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
