package dogpark.web;

import dogpark.model.dtos.AddBreedDTO;
import dogpark.model.entity.BreedEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BreedControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllBreed() throws Exception {

        mockMvc.perform(get("/breeds/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allBreeds"))
                .andExpect(view().name("all-breeds"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testGetBreedAdd() throws Exception {

        mockMvc.perform(get("/breeds/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-breed"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testAddBreed() throws Exception {

        AddBreedDTO addBreedDTO = AddBreedDTO.builder()
                .name("Chihuahua")
                .imgURL("chihuahua.png")
                .grooming(40)
                .hunting(50)
                .agility(70)
                .build();

        mockMvc.perform(post("/breeds/add")
                        .flashAttr("addBreedDTO", addBreedDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/breeds/all"));
    }

}