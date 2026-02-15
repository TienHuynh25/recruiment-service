package vn.unigap;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import vn.unigap.api.repository.EmployerRepository;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = EmployerRepository.class)
@ComponentScan(basePackages = {"vn.unigap", "vn.unigap.api.repository"})
public class SpringDataJpaConfig {



}
