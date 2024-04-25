package vn.unigap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import vn.unigap.api.controller.EmployerController;
import vn.unigap.api.dto.in.EmployerInputDTO;
import vn.unigap.api.dto.out.EmployerOutputDTO;
import vn.unigap.api.service.EmployerService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployerController.class)
class WebMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployerService service;

    @Test
    void CreateEmployerTest() throws Exception {

        EmployerInputDTO employerInputDTO = EmployerInputDTO.builder()
                .name("test")
                .email("test@gmail.com")
                .description("test")
                .build();

        // Create a mock Employer object with the expected values
        EmployerOutputDTO mockEmployer = EmployerOutputDTO.builder()
                .name("test")
                .email("test@gmail.com")
                .description("test")
                .build();

        // Adjust the when clause to return the mock Employer object
        when(service.createEmployer(employerInputDTO)).thenReturn(mockEmployer);

        // Make a POST request to the createEmployer endpoint with the necessary request body
        this.mockMvc.perform(post("/api/v1/employer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employerInputDTO)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void GetAllEmployerTest() throws Exception {
        this.mockMvc.perform(get("/api/v1/employer"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void GetEmployerByIdTest() throws Exception {
        this.mockMvc.perform(get("/api/v1/employer/3093562"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
