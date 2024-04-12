package vn.unigap.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.unigap.api.dto.in.EmployerInputDTO;
import vn.unigap.api.dto.out.EmployerOutputDTO;
import vn.unigap.api.entity.EmployerEntity;
import vn.unigap.api.repository.EmployerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    public EmployerOutputDTO createEmployer(EmployerInputDTO employerInputDTO) {
        EmployerEntity employerEntity = EmployerEntity.builder()
            .email(employerInputDTO.getEmail())
            .name(employerInputDTO.getName())
            .province(employerInputDTO.getProvince())
            .description(employerInputDTO.getDescription())
            .build();
        employerRepository.save(employerEntity);
        return EmployerOutputDTO.fromEntity(employerEntity);
    }

    public List<EmployerOutputDTO> getAllEmployer(Pageable pageable) {
        List<EmployerEntity> employerEntityList = employerRepository.findAll((org.springframework.data.domain.Pageable) pageable).getContent();
        return employerEntityList.stream().map(EmployerOutputDTO::fromEntity).collect(Collectors.toList());
    }

    public EmployerOutputDTO getEmployerById(Long id) {
        Optional<EmployerEntity> employerEntity = employerRepository.findById(id);
        return employerEntity.map(EmployerOutputDTO::fromEntity).orElse(null);
    }

    public EmployerOutputDTO updateEmployerById(Long id, EmployerInputDTO employerInputDTO) {
        Optional<EmployerEntity> employerEntity = employerRepository.findById(id);
        if (employerEntity.isPresent()) {
            EmployerEntity employer = employerEntity.get();
            employer.setEmail(employerInputDTO.getEmail());
            employer.setName(employerInputDTO.getName());
            employer.setProvince(employerInputDTO.getProvince());
            employer.setDescription(employerInputDTO.getDescription());
            employerRepository.save(employer);
            return EmployerOutputDTO.fromEntity(employer);
        }
        return null;
    }

    public void deleteEmployerById(Long id) {
        employerRepository.deleteById(id);
    }

    public void deleteAllEmployer() {
        employerRepository.deleteAll();
    }

    public long countEmployer() {
        return employerRepository.count();
    }

    public boolean isEmployerExist(Long id) {
        return employerRepository.existsById(id);
    }

}
