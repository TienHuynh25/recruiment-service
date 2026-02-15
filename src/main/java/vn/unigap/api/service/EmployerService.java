package vn.unigap.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.unigap.api.common.Constants;
import vn.unigap.api.dto.in.EmployerInputDTO;
import vn.unigap.api.dto.out.EmployerOutputDTO;
import vn.unigap.api.entity.EmployerEntity;
import vn.unigap.api.entity.JobProvinceEntity;
import vn.unigap.api.exception.DuplicateResourceException;
import vn.unigap.api.exception.ResourceNotFoundException;
import vn.unigap.api.repository.EmployerRepository;
import vn.unigap.api.repository.JobProvinceRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private JobProvinceRepository jobProvinceRepository;

    public EmployerOutputDTO createEmployer(EmployerInputDTO employerInputDTO) {
        // Check if email already exists
        if (employerRepository.findByEmail(employerInputDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException(Constants.DUPLICATE_EMAIL + employerInputDTO.getEmail());
        }

        // Validate and get province
        JobProvinceEntity province = jobProvinceRepository.findById(employerInputDTO.getProvinceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.JOB_PROVINCE_NOT_FOUND + employerInputDTO.getProvinceId()));

        EmployerEntity employerEntity = EmployerEntity.builder()
            .email(employerInputDTO.getEmail())
            .name(employerInputDTO.getName())
            .province(province)
            .description(employerInputDTO.getDescription())
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();
        employerRepository.save(employerEntity);
        return EmployerOutputDTO.fromEntity(employerEntity);
    }

    public List<EmployerOutputDTO> getAllEmployer(Pageable pageable) {
        List<EmployerEntity> employerEntityList = employerRepository.findAll((org.springframework.data.domain.Pageable) pageable).getContent();
        return employerEntityList.stream().map(EmployerOutputDTO::fromEntity).collect(Collectors.toList());
    }

    public EmployerOutputDTO getEmployerById(Long id) {
        EmployerEntity employerEntity = employerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.EMPLOYER_NOT_FOUND + id));
        return EmployerOutputDTO.fromEntity(employerEntity);
    }

    public EmployerOutputDTO updateEmployerById(Long id, EmployerInputDTO employerInputDTO) {
        EmployerEntity employer = employerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.EMPLOYER_NOT_FOUND + id));

        // Check if email is being changed and if new email already exists
        if (!employer.getEmail().equals(employerInputDTO.getEmail())) {
            if (employerRepository.findByEmail(employerInputDTO.getEmail()).isPresent()) {
                throw new DuplicateResourceException(Constants.DUPLICATE_EMAIL + employerInputDTO.getEmail());
            }
        }

        // Validate and get province
        JobProvinceEntity province = jobProvinceRepository.findById(employerInputDTO.getProvinceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.JOB_PROVINCE_NOT_FOUND + employerInputDTO.getProvinceId()));

        employer.setEmail(employerInputDTO.getEmail());
        employer.setName(employerInputDTO.getName());
        employer.setProvince(province);
        employer.setDescription(employerInputDTO.getDescription());
        employer.setUpdatedAt(new Date());
        employerRepository.save(employer);
        return EmployerOutputDTO.fromEntity(employer);
    }

    public void deleteEmployerById(Long id) {
        if (!employerRepository.existsById(id)) {
            throw new ResourceNotFoundException(Constants.EMPLOYER_NOT_FOUND + id);
        }
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
