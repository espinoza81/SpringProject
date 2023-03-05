package dogpark.web;

import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.DogDTO;
import dogpark.service.DogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DogController {
    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping("/users/dog")
    public String register() {
        return "my_dogs";
    }

    @PreAuthorize("@dogService.isOwner(#principal.username, #dogId)")
    @GetMapping("/dog/{id}")
    public String dog(@PathVariable("id") Long dogId,
                      Model model) {

        DogDTO dog = dogService.getDogInfoById(dogId).
                orElseThrow(() -> new ObjectNotFoundException("Dog with ID "+ dogId + "not found"));

        System.out.println(dog);
        model.addAttribute("dog", dog);
        return "dog";
    }
}