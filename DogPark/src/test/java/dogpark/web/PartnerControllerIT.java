package dogpark.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PartnerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "espinoza81@yahoo.com")
    void dogsForStud() throws Exception {
        mockMvc.perform(get("/market/stud"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("studOffers"))
                .andExpect(model().attributeExists("dogsOwner"))
                .andExpect(model().attributeExists("dogsSexF"))
                .andExpect(view().name("stud_offers"));
    }

}