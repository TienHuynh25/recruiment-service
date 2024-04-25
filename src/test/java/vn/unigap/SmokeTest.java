package vn.unigap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import vn.unigap.api.controller.EmployerController;
import vn.unigap.api.dto.in.EmployerInputDTO;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import vn.unigap.api.dto.out.EmployerOutputDTO;


@SpringBootTest
class SmokeTest {

	@Autowired
	private EmployerController employerController;

	@Test
	void contextLoads() {
		assert employerController != null;
	}

	@Test
	void testCreateEmployer() {
		// Arrange
		EmployerInputDTO employerInputDTO = EmployerInputDTO.builder()
				.name("test")
				.email("test@gmail.com")
				.description("test")
				.build();

		// Act
		ResponseEntity<EmployerOutputDTO> result = employerController.createEmployer(employerInputDTO);


		// Assert
		Assertions.assertNotNull(result, "Expected non-null result from createEmployer");

		Assertions.assertNotNull(result.getBody(), "Expected non-null body from createEmployer");
		assertThat(result.getBody().getName()).isEqualTo("test");

		employerController.deleteEmployerById(result.getBody().getId());
	}

	@Test
	void testGetAllEmployer() {
		// Act
		Object result = employerController.getAllEmployer(Pageable.ofSize(10));

		// Assert
		Assertions.assertNotNull(result, "Expected non-null result from getAllEmployer");
	}

	@Test
	void testGetEmployerById() {
		// Act
		Object result = employerController.getEmployerById(1L);

		// Assert
		Assertions.assertNotNull(result, "Expected non-null result from getEmployerById");
	}

}
