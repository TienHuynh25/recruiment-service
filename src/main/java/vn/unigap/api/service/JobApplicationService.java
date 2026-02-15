package vn.unigap.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.unigap.api.common.Constants;
import vn.unigap.api.dto.in.JobApplicationInputDTO;
import vn.unigap.api.dto.in.JobApplicationStatusUpdateDTO;
import vn.unigap.api.dto.out.JobApplicationOutputDTO;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.entity.JobApplicationEntity;
import vn.unigap.api.entity.JobEntity;
import vn.unigap.api.entity.ResumeEntity;
import vn.unigap.api.exception.DuplicateResourceException;
import vn.unigap.api.exception.ResourceNotFoundException;
import vn.unigap.api.exception.ValidationException;
import vn.unigap.api.repository.JobApplicationRepository;
import vn.unigap.api.repository.JobRepository;
import vn.unigap.api.repository.ResumeRepository;

import java.util.Date;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Transactional
    public JobApplicationOutputDTO applyForJob(JobApplicationInputDTO inputDTO) {
        // Validate job exists
        JobEntity job = jobRepository.findById(inputDTO.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.JOB_NOT_FOUND + inputDTO.getJobId()));

        // Validate resume exists
        ResumeEntity resume = resumeRepository.findById(inputDTO.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.RESUME_NOT_FOUND + inputDTO.getResumeId()));

        // Check if job is expired
        if (job.getExpiredAt().before(new Date())) {
            throw new ValidationException("Cannot apply for expired job");
        }

        // Check for duplicate application
        if (jobApplicationRepository.findByJobIdAndResumeId(inputDTO.getJobId(), inputDTO.getResumeId()).isPresent()) {
            throw new DuplicateResourceException(Constants.DUPLICATE_APPLICATION);
        }

        JobApplicationEntity application = JobApplicationEntity.builder()
                .job(job)
                .resume(resume)
                .status(JobApplicationEntity.ApplicationStatus.PENDING)
                .appliedAt(new Date())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        jobApplicationRepository.save(application);
        return JobApplicationOutputDTO.fromEntity(application);
    }

    public PageResponse<JobApplicationOutputDTO> getAllApplications(
            Long jobId,
            Long resumeId,
            String status,
            Pageable pageable) {
        JobApplicationEntity.ApplicationStatus applicationStatus = null;
        if (status != null) {
            try {
                applicationStatus = JobApplicationEntity.ApplicationStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid status: " + status);
            }
        }

        Page<JobApplicationEntity> page = jobApplicationRepository.findByFilters(
                jobId, resumeId, applicationStatus, pageable);
        Page<JobApplicationOutputDTO> dtoPage = page.map(JobApplicationOutputDTO::fromEntity);
        return PageResponse.fromPage(dtoPage);
    }

    public JobApplicationOutputDTO getApplicationById(Long id) {
        JobApplicationEntity application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.JOB_APPLICATION_NOT_FOUND + id));
        return JobApplicationOutputDTO.fromEntity(application);
    }

    @Transactional
    public JobApplicationOutputDTO updateApplicationStatus(Long id, JobApplicationStatusUpdateDTO statusUpdateDTO) {
        JobApplicationEntity application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.JOB_APPLICATION_NOT_FOUND + id));

        application.setStatus(statusUpdateDTO.getStatus());
        application.setUpdatedAt(new Date());

        jobApplicationRepository.save(application);
        return JobApplicationOutputDTO.fromEntity(application);
    }

    public void withdrawApplication(Long id) {
        if (!jobApplicationRepository.existsById(id)) {
            throw new ResourceNotFoundException(Constants.JOB_APPLICATION_NOT_FOUND + id);
        }
        jobApplicationRepository.deleteById(id);
    }
}
