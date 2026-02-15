package vn.unigap.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.unigap.api.common.Constants;
import vn.unigap.api.dto.in.JobInputDTO;
import vn.unigap.api.dto.out.JobOutputDTO;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.entity.EmployerEntity;
import vn.unigap.api.entity.JobEntity;
import vn.unigap.api.entity.JobFieldEntity;
import vn.unigap.api.entity.JobProvinceEntity;
import vn.unigap.api.exception.ResourceNotFoundException;
import vn.unigap.api.exception.ValidationException;
import vn.unigap.api.repository.EmployerRepository;
import vn.unigap.api.repository.JobFieldRepository;
import vn.unigap.api.repository.JobProvinceRepository;
import vn.unigap.api.repository.JobRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private JobFieldRepository jobFieldRepository;

    @Autowired
    private JobProvinceRepository jobProvinceRepository;

    @Transactional
    public JobOutputDTO createJob(JobInputDTO jobInputDTO) {
        // Validate employer exists
        EmployerEntity employer = employerRepository.findById(jobInputDTO.getEmployerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.EMPLOYER_NOT_FOUND + jobInputDTO.getEmployerId()));

        // Validate fields exist
        List<JobFieldEntity> fields = validateAndGetFields(jobInputDTO.getFieldIds());

        // Validate provinces exist
        List<JobProvinceEntity> provinces = validateAndGetProvinces(jobInputDTO.getProvinceIds());

        // Validate expiration date is in the future
        if (jobInputDTO.getExpiredAt().before(new Date())) {
            throw new ValidationException(Constants.VALIDATE_EXPIRED_AT_FUTURE);
        }

        JobEntity jobEntity = JobEntity.builder()
                .employer(employer)
                .title(jobInputDTO.getTitle())
                .quantity(jobInputDTO.getQuantity())
                .description(jobInputDTO.getDescription())
                .fields(fields)
                .provinces(provinces)
                .salary(jobInputDTO.getSalary())
                .expiredAt(jobInputDTO.getExpiredAt())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        jobRepository.save(jobEntity);
        return JobOutputDTO.fromEntity(jobEntity);
    }

    public PageResponse<JobOutputDTO> getAllJobs(Long employerId, Long fieldId, Long provinceId, Pageable pageable) {
        Page<JobEntity> page;

        if (employerId != null || fieldId != null || provinceId != null) {
            page = jobRepository.findByFilters(employerId, fieldId, provinceId, pageable);
        } else {
            page = jobRepository.findAll(pageable);
        }

        Page<JobOutputDTO> dtoPage = page.map(JobOutputDTO::fromEntity);
        return PageResponse.fromPage(dtoPage);
    }

    public JobOutputDTO getJobById(Long id) {
        JobEntity jobEntity = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.JOB_NOT_FOUND + id));
        return JobOutputDTO.fromEntity(jobEntity);
    }

    @Transactional
    public JobOutputDTO updateJob(Long id, JobInputDTO jobInputDTO) {
        JobEntity jobEntity = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.JOB_NOT_FOUND + id));

        // Validate employer exists
        EmployerEntity employer = employerRepository.findById(jobInputDTO.getEmployerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.EMPLOYER_NOT_FOUND + jobInputDTO.getEmployerId()));

        // Validate fields exist
        List<JobFieldEntity> fields = validateAndGetFields(jobInputDTO.getFieldIds());

        // Validate provinces exist
        List<JobProvinceEntity> provinces = validateAndGetProvinces(jobInputDTO.getProvinceIds());

        // Validate expiration date is in the future
        if (jobInputDTO.getExpiredAt().before(new Date())) {
            throw new ValidationException(Constants.VALIDATE_EXPIRED_AT_FUTURE);
        }

        jobEntity.setEmployer(employer);
        jobEntity.setTitle(jobInputDTO.getTitle());
        jobEntity.setQuantity(jobInputDTO.getQuantity());
        jobEntity.setDescription(jobInputDTO.getDescription());
        jobEntity.setFields(fields);
        jobEntity.setProvinces(provinces);
        jobEntity.setSalary(jobInputDTO.getSalary());
        jobEntity.setExpiredAt(jobInputDTO.getExpiredAt());
        jobEntity.setUpdatedAt(new Date());

        jobRepository.save(jobEntity);
        return JobOutputDTO.fromEntity(jobEntity);
    }

    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new ResourceNotFoundException(Constants.JOB_NOT_FOUND + id);
        }
        jobRepository.deleteById(id);
    }

    private List<JobFieldEntity> validateAndGetFields(List<Long> fieldIds) {
        List<JobFieldEntity> fields = jobFieldRepository.findAllById(fieldIds);
        if (fields.size() != fieldIds.size()) {
            throw new ResourceNotFoundException("One or more job fields not found");
        }
        return fields;
    }

    private List<JobProvinceEntity> validateAndGetProvinces(List<Long> provinceIds) {
        List<JobProvinceEntity> provinces = jobProvinceRepository.findAllById(provinceIds);
        if (provinces.size() != provinceIds.size()) {
            throw new ResourceNotFoundException("One or more provinces not found");
        }
        return provinces;
    }
}
