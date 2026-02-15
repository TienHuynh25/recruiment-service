package vn.unigap.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.unigap.api.common.Constants;
import vn.unigap.api.dto.out.JobProvinceOutputDTO;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.entity.JobProvinceEntity;
import vn.unigap.api.exception.ResourceNotFoundException;
import vn.unigap.api.repository.JobProvinceRepository;

@Service
public class JobProvinceService {

    @Autowired
    private JobProvinceRepository jobProvinceRepository;

    public PageResponse<JobProvinceOutputDTO> getAllProvinces(Pageable pageable) {
        Page<JobProvinceEntity> page = jobProvinceRepository.findAll(pageable);
        Page<JobProvinceOutputDTO> dtoPage = page.map(JobProvinceOutputDTO::fromEntity);
        return PageResponse.fromPage(dtoPage);
    }

    public JobProvinceOutputDTO getProvinceById(Long id) {
        JobProvinceEntity entity = jobProvinceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.JOB_PROVINCE_NOT_FOUND + id));
        return JobProvinceOutputDTO.fromEntity(entity);
    }

    public boolean existsById(Long id) {
        return jobProvinceRepository.existsById(id);
    }
}
