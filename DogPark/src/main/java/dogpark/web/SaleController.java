package dogpark.web;

import dogpark.model.dtos.*;
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

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @ModelAttribute("addSaleDTO")
    public AddSaleStudDTO initAddSaleDTO(){
        return new AddSaleStudDTO();
    }

    @GetMapping("/market/sale")
    public String dogsForSale(Model model,
                       @AuthenticationPrincipal UserDetails user) {

        List<DogWithPriceDTO> dogsForSale = saleService.getDogsForSale(user.getUsername());
        List<DogWithNameIdDTO> dogsOwnerForSale = saleService.getDogsNotForSale(user.getUsername());

        model.addAttribute("dogsForSale", dogsForSale);
        model.addAttribute("dogsOwner", dogsOwnerForSale);

        return "sale_offers";
    }

    @PreAuthorize("@dogService.isOwner(#userDetails.username, #addSaleDTO.dogId)")
    @PostMapping("/dog/sale")
    public String saleSingleDog(@Valid AddSaleStudDTO addSaleDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal UserDetails userDetails) {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("addSaleDTO", addSaleDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addSaleDTO", bindingResult);

            return "redirect:/market/sale";
        }

        saleService.createdSale(addSaleDTO);

        return "redirect:/market/sale";
    }

    @PreAuthorize("!@dogService.isOwner(#userDetails.username, #dogId) && !@dogService.isNotForSale(#dogId)")
    @PostMapping("/dog/{dogId}/sale")
    public String byeDog(@PathVariable("dogId") Long dogId,
                                   @AuthenticationPrincipal UserDetails userDetails){

        saleService.buyDog(dogId, userDetails.getUsername());

        return "redirect:/market/sale";
    }

    @PreAuthorize("@dogService.isOwner(#userDetails.username, #dogId)")
    @PostMapping("/dog/{dogId}/delete_sale")
    public String deleteSale(@PathVariable("dogId") Long dogId,
                             @AuthenticationPrincipal UserDetails userDetails){

        saleService.deleteSale(dogId);

        return "redirect:/users/shelter";
    }
}
