package vn.unigap.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.unigap.api.common.Constants;
import vn.unigap.api.dto.out.JobFieldOutputDTO;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.entity.JobFieldEntity;
import vn.unigap.api.exception.ResourceNotFoundException;
import vn.unigap.api.repository.JobFieldRepository;

@Service
public class JobFieldService {

    @Autowired
    private JobFieldRepository jobFieldRepository;

    public PageResponse<JobFieldOutputDTO> getAllJobFields(Pageable pageable) {
        Page<JobFieldEntity> page = jobFieldRepository.findAll(pageable);
        Page<JobFieldOutputDTO> dtoPage = page.map(JobFieldOutputDTO::fromEntity);
        return PageResponse.fromPage(dtoPage);
    }

    public JobFieldOutputDTO getJobFieldById(Long id) {
        JobFieldEntity entity = jobFieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.JOB_FIELD_NOT_FOUND + id));
        return JobFieldOutputDTO.fromEntity(entity);
    }

    public boolean existsById(Long id) {
        return jobFieldRepository.existsById(id);
    }
}
