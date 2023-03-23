package dogpark.web;

import dogpark.model.dtos.*;
import dogpark.model.enums.SexEnum;
import dogpark.service.CompetitionService;
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
public class CompetitionController {
    private final DogService dogService;
    public final CompetitionService competitionService;

    public CompetitionController(DogService dogService,
                                 CompetitionService competitionService) {
        this.dogService = dogService;
        this.competitionService = competitionService;
    }

    @ModelAttribute("participantDTO")
    public ParticipantDTO initAddStudDTO(){
        return new ParticipantDTO();
    }
    @GetMapping("/competition")
    public String competition(Model model,
                              @AuthenticationPrincipal UserDetails user) {

        List<DogWithNameIdDTO> dogsOwner = this.competitionService.getDogsForCompetition(user.getUsername());
        List<DogWithNameCupDTO> dogsEnrolled = this.competitionService.getEnrolledDogs();

        model.addAttribute("dogsEnrolled", dogsEnrolled);
        model.addAttribute("dogsOwner", dogsOwner);


        return "competition";
    }

    @PreAuthorize("isOwner(#participantDTO.dogId)")
    @PostMapping("/competition")
    public String enterDog(@Valid ParticipantDTO participantDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("participantDTO", participantDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.participantDTO", bindingResult);

            return "redirect:/competition";
        }

        this.competitionService.enrollDog(participantDTO);

        return "redirect:/competition";
    }
}
