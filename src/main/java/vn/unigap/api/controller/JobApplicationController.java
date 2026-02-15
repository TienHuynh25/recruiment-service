package vn.unigap.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.unigap.api.dto.in.JobApplicationInputDTO;
import vn.unigap.api.dto.in.JobApplicationStatusUpdateDTO;
import vn.unigap.api.dto.out.JobApplicationOutputDTO;
import vn.unigap.api.dto.response.ApiResponse;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.service.JobApplicationService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Job Applications", description = "Job application management APIs")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @PostMapping(path = "/applications", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<JobApplicationOutputDTO>> applyForJob(
            @Valid @RequestBody JobApplicationInputDTO inputDTO) {
        JobApplicationOutputDTO outputDTO = jobApplicationService.applyForJob(inputDTO);
        return new ResponseEntity<>(ApiResponse.created(outputDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "/applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<JobApplicationOutputDTO>> getAllApplications(
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) Long resumeId,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        PageResponse<JobApplicationOutputDTO> response = jobApplicationService.getAllApplications(
                jobId, resumeId, status, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/applications/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<JobApplicationOutputDTO>> getApplicationById(@PathVariable Long id) {
        JobApplicationOutputDTO outputDTO = jobApplicationService.getApplicationById(id);
        return new ResponseEntity<>(ApiResponse.success(outputDTO), HttpStatus.OK);
    }

    @PutMapping(path = "/applications/{id}/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<JobApplicationOutputDTO>> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationStatusUpdateDTO statusUpdateDTO) {
        JobApplicationOutputDTO outputDTO = jobApplicationService.updateApplicationStatus(id, statusUpdateDTO);
        return new ResponseEntity<>(ApiResponse.success(outputDTO), HttpStatus.OK);
    }

    @DeleteMapping(path = "/applications/{id}")
    public ResponseEntity<ApiResponse<Void>> withdrawApplication(@PathVariable Long id) {
        jobApplicationService.withdrawApplication(id);
        return new ResponseEntity<>(ApiResponse.success(null), HttpStatus.OK);
    }
}
