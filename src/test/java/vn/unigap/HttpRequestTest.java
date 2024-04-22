package vn.unigap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import vn.unigap.api.entity.EmployerEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllEmployerTest() throws Exception {
        String response = this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/employer",
                String.class);
        assertThat(response).isNotNull();

        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, EmployerEntity.class);
        List<EmployerEntity> employers = mapper.readValue(response, listType);
        assertThat(employers).isNotNull();
    }

    @Test
    void createEmployerTest() throws Exception {
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/api/v1/employer",
                "{\"name\":\"test\",\"email\":\"test@gmail.com\",\"description\":\"test\"}", String.class)).isNotNull();
    }

    @Test
    void getEmployerByIdTest() throws Exception {
        String response = this.restTemplate.getForObject("http://localhost:" + port + "/api/v1/employer/1",
                String.class);
        assertThat(response).isNotNull();

        ObjectMapper mapper = new ObjectMapper();
        EmployerEntity employer = mapper.readValue(response, EmployerEntity.class);
        assertThat(employer).isNotNull();
    }


}
