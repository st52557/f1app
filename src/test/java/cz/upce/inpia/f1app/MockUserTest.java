package cz.upce.inpia.f1app;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.upce.inpia.f1app.controller.UserController;

import cz.upce.inpia.f1app.entity.User;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MockUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void hashingPasswordTest() throws Exception {

        User user = new User();
        user.setName("Test1");
        user.setPassword("123456");

        String jsonUser = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().is2xxSuccessful());

        Assertions.assertThat(userRepository.findByName("Test1").getPassword()).isNotEqualTo("123456");

    }



}
