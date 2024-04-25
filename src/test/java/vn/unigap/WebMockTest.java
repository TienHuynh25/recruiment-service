package vn.unigap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import vn.unigap.api.controller.EmployerController;
import vn.unigap.api.dto.in.EmployerInputDTO;
import vn.unigap.api.service.EmployerService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
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
        when(service.createEmployer(employerInputDTO)).thenReturn(null);
        this.mockMvc.perform(get("/api/v1/employer/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("test")));
    }
}
