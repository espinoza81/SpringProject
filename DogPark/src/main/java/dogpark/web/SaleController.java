package dogpark.web;

import dogpark.model.dtos.*;
import dogpark.service.DogService;
import dogpark.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SaleController {

    private final SaleService saleService;
    private final DogService dogService;

    public SaleController(SaleService saleService,
                          DogService dogService) {
        this.saleService = saleService;
        this.dogService = dogService;
    }

    @ModelAttribute("addSaleDTO")
    public AddSaleStudDTO initAddSaleDTO(){
        return new AddSaleStudDTO();
    }

    @GetMapping("/market/sale")
    public String dogsForSale(Model model,
                       @AuthenticationPrincipal UserDetails user) {

        List<DogWithPriceDTO> dogsForSale = dogService.getDogsForSale(user.getUsername());
        List<DogWithNameIdDTO> dogsOwnerForSale = this.dogService.getDogsNotForSale(user.getUsername());

        model.addAttribute("dogsForSale", dogsForSale);
        model.addAttribute("dogsOwner", dogsOwnerForSale);

        return "sale_offers";
    }

    @PreAuthorize("isOwner(#addSaleDTO.dogId)")
    @PostMapping("/dog/sale")
    public String saleSingleDog(@Valid AddSaleStudDTO addSaleDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("addSaleDTO", addSaleDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addSaleDTO", bindingResult);

            return "redirect:/market/sale";
        }

        this.dogService.createdSale(addSaleDTO);

        return "redirect:/market/sale";
    }

    @PreAuthorize("!isOwner(#dogId)")
    @PostMapping("/dog/{dogId}/sale")
    public String byeDog(@PathVariable("dogId") Long dogId,
                                   @AuthenticationPrincipal UserDetails user){

        //TODO: check if user have enough money and dog is for sale

        dogService.buyDog(dogId, user.getUsername());

        return "redirect:/market/sale";
    }
}
