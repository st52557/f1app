package cz.upce.inpia.f1app;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.upce.inpia.f1app.controller.UserController;

import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.User;
import cz.upce.inpia.f1app.repository.DriverRepository;
import cz.upce.inpia.f1app.repository.UserRepository;
import cz.upce.inpia.f1app.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import javax.validation.ConstraintViolationException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MockDriverTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username="lukas",password = "lukas")
    void setDriverCodeTest() throws Exception {


        Driver driver = new Driver();
        driver.setName("Lando");
        driver.setCode("LAN");
        driver.setBorn(1997);

        String jsonDriver = mapper.writeValueAsString(driver);

        MvcResult result = this.mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDriver)).andReturn();

        String resJson = result.getResponse().getContentAsString();
        Driver resDriver = new ObjectMapper().readValue(resJson, Driver.class);
        Assertions.assertThat(resDriver.getCode()).isEqualTo("LAN");

    }

    @Test
    @WithMockUser(username="lukas",password = "lukas")
    void setDriverCodeTestViolatingConstraint() throws Exception {


        Driver driver = new Driver();
        driver.setName("Lando");
        driver.setCode("LAND");
        driver.setBorn(1997);

        String jsonDriver = mapper.writeValueAsString(driver);
        this.mockMvc.perform(post("/driver")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDriver)).andExpect(MockMvcResultMatchers.status().isBadRequest());


    }



}
