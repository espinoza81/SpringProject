package dogpark.web;

import dogpark.model.dtos.AddSaleStudDTO;
import dogpark.model.dtos.BreedingIdsDTO;
import dogpark.model.dtos.DogWithNameIdDTO;
import dogpark.model.dtos.SaleStudDTO;
import dogpark.model.enums.SexEnum;
import dogpark.service.PartnerService;
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
public class PartnerController {
    private final PartnerService partnerService;
    private final DogService dogService;

    public PartnerController(PartnerService partnerService,
                             DogService dogService) {
        this.partnerService = partnerService;
        this.dogService = dogService;
    }

    @ModelAttribute("addStudDTO")
    public AddSaleStudDTO initAddStudDTO(){
        return new AddSaleStudDTO();
    }

    @ModelAttribute("breedingIdsDTO")
    public BreedingIdsDTO initBreedingIdsDTO(){
        return new BreedingIdsDTO();
    }

    @GetMapping("/market/stud")
    public String dogsForStud(Model model,
                              @AuthenticationPrincipal UserDetails user) {

        List<SaleStudDTO> studOffers = partnerService.getDogsForStud();
        List<DogWithNameIdDTO> dogsOwnerForStud = partnerService.getDogsSex(user.getUsername(), SexEnum.M);
        List<DogWithNameIdDTO> dogsOwnerSexF = partnerService.getDogsSex(user.getUsername(), SexEnum.F);

        model.addAttribute("studOffers", studOffers);
        model.addAttribute("dogsOwner", dogsOwnerForStud);
        model.addAttribute("dogsSexF", dogsOwnerSexF);

        return "stud_offers";
    }

    @PreAuthorize("@dogService.isOwner(#userDetails.username, #addStudDTO.dogId)")
    @PostMapping("/dog/stud")
    public String studSingleDog(@Valid AddSaleStudDTO addStudDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal UserDetails userDetails) {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("addStudDTO", addStudDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addStudDTO", bindingResult);

            return "redirect:/market/stud";
        }

        partnerService.createdStud(addStudDTO);

        return "redirect:/market/stud";
    }

    @PostMapping("/breeding")
    public String breedSingleDog(@Valid BreedingIdsDTO breedingIdsDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal UserDetails user,
                                 Model model) {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("breedingIdsDTO", breedingIdsDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.breedingIdsDTO", bindingResult);

            return "redirect:/market/stud";
        }

        partnerService.breedDog(breedingIdsDTO, user.getUsername());
//        model.addAttribute("puppy", puppy);
        return "redirect:/users/dog";
    }
}
