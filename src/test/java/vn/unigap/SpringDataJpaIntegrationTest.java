package vn.unigap;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import vn.unigap.api.repository.EmployerRepository;

@ContextConfiguration(classes = SpringDataJpaConfig.class)
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback
public class SpringDataJpaIntegrationTest {

    @Autowired
    private EmployerRepository employerRepository;

    // add test finding an employer by id
    @Test
    void testFindEmployerById() {
        // Arrange
        Long id = 3093562L;

        // Act
        var result = employerRepository.findById(id);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.get().getId()).isEqualTo(id);
    }


}
