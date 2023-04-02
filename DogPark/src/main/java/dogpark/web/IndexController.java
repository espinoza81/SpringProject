package dogpark.web;

import dogpark.model.dtos.DogWithNameCupDTO;
import dogpark.service.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping("/")
    public String home(Model model) {

        List<DogWithNameCupDTO> bestDogs = indexService.getTopDogs();
        model.addAttribute("bestDogs", bestDogs);

        return "index";
    }
}
