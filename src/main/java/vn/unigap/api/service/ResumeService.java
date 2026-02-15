package vn.unigap.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.unigap.api.common.Constants;
import vn.unigap.api.dto.in.ResumeInputDTO;
import vn.unigap.api.dto.out.ResumeOutputDTO;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.entity.JobFieldEntity;
import vn.unigap.api.entity.JobProvinceEntity;
import vn.unigap.api.entity.ResumeEntity;
import vn.unigap.api.entity.SeekerEntity;
import vn.unigap.api.exception.ResourceNotFoundException;
import vn.unigap.api.repository.JobFieldRepository;
import vn.unigap.api.repository.JobProvinceRepository;
import vn.unigap.api.repository.ResumeRepository;
import vn.unigap.api.repository.SeekerRepository;

import java.util.Date;
import java.util.List;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private SeekerRepository seekerRepository;

    @Autowired
    private JobFieldRepository jobFieldRepository;

    @Autowired
    private JobProvinceRepository jobProvinceRepository;

    @Transactional
    public ResumeOutputDTO createResume(ResumeInputDTO resumeInputDTO) {
        // Validate seeker exists
        SeekerEntity seeker = seekerRepository.findById(resumeInputDTO.getSeekerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.SEEKER_NOT_FOUND + resumeInputDTO.getSeekerId()));

        // Validate fields exist
        List<JobFieldEntity> fields = validateAndGetFields(resumeInputDTO.getFieldIds());

        // Validate provinces exist
        List<JobProvinceEntity> provinces = validateAndGetProvinces(resumeInputDTO.getProvinceIds());

        ResumeEntity resumeEntity = ResumeEntity.builder()
                .seeker(seeker)
                .careerObj(resumeInputDTO.getCareerObj())
                .title(resumeInputDTO.getTitle())
                .salary(resumeInputDTO.getSalary())
                .fields(fields)
                .provinces(provinces)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        resumeRepository.save(resumeEntity);
        return ResumeOutputDTO.fromEntity(resumeEntity);
    }

    public PageResponse<ResumeOutputDTO> getAllResumes(Long seekerId, Long fieldId, Long provinceId, Pageable pageable) {
        Page<ResumeEntity> page;

        if (seekerId != null || fieldId != null || provinceId != null) {
            page = resumeRepository.findByFilters(seekerId, fieldId, provinceId, pageable);
        } else {
            page = resumeRepository.findAll(pageable);
        }

        Page<ResumeOutputDTO> dtoPage = page.map(ResumeOutputDTO::fromEntity);
        return PageResponse.fromPage(dtoPage);
    }

    public ResumeOutputDTO getResumeById(Long id) {
        ResumeEntity resumeEntity = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.RESUME_NOT_FOUND + id));
        return ResumeOutputDTO.fromEntity(resumeEntity);
    }

    @Transactional
    public ResumeOutputDTO updateResume(Long id, ResumeInputDTO resumeInputDTO) {
        ResumeEntity resumeEntity = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.RESUME_NOT_FOUND + id));

        // Validate seeker exists
        SeekerEntity seeker = seekerRepository.findById(resumeInputDTO.getSeekerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.SEEKER_NOT_FOUND + resumeInputDTO.getSeekerId()));

        // Validate fields exist
        List<JobFieldEntity> fields = validateAndGetFields(resumeInputDTO.getFieldIds());

        // Validate provinces exist
        List<JobProvinceEntity> provinces = validateAndGetProvinces(resumeInputDTO.getProvinceIds());

        resumeEntity.setSeeker(seeker);
        resumeEntity.setCareerObj(resumeInputDTO.getCareerObj());
        resumeEntity.setTitle(resumeInputDTO.getTitle());
        resumeEntity.setSalary(resumeInputDTO.getSalary());
        resumeEntity.setFields(fields);
        resumeEntity.setProvinces(provinces);
        resumeEntity.setUpdatedAt(new Date());

        resumeRepository.save(resumeEntity);
        return ResumeOutputDTO.fromEntity(resumeEntity);
    }

    public void deleteResume(Long id) {
        if (!resumeRepository.existsById(id)) {
            throw new ResourceNotFoundException(Constants.RESUME_NOT_FOUND + id);
        }
        resumeRepository.deleteById(id);
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
