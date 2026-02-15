package vn.unigap.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.unigap.api.dto.in.JobInputDTO;
import vn.unigap.api.dto.out.JobOutputDTO;
import vn.unigap.api.dto.response.ApiResponse;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.service.JobService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Jobs", description = "Job management APIs for creating and managing job postings")
public class JobController {

    @Autowired
    private JobService jobService;

    @Operation(summary = "Create a new job", description = "Creates a new job posting (Employer role required)")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(path = "/jobs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<JobOutputDTO>> createJob(@Valid @RequestBody JobInputDTO jobInputDTO) {
        JobOutputDTO jobOutputDTO = jobService.createJob(jobInputDTO);
        return new ResponseEntity<>(ApiResponse.created(jobOutputDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all jobs", description = "Retrieves all jobs with optional filtering by employer, field, or province")
    @GetMapping(path = "/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<JobOutputDTO>> getAllJobs(
            @Parameter(description = "Filter by employer ID") @RequestParam(required = false) Long employerId,
            @Parameter(description = "Filter by job field ID") @RequestParam(required = false) Long fieldId,
            @Parameter(description = "Filter by province ID") @RequestParam(required = false) Long provinceId,
            Pageable pageable) {
        PageResponse<JobOutputDTO> response = jobService.getAllJobs(employerId, fieldId, provinceId, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/jobs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<JobOutputDTO>> getJobById(@PathVariable Long id) {
        JobOutputDTO jobOutputDTO = jobService.getJobById(id);
        return new ResponseEntity<>(ApiResponse.success(jobOutputDTO), HttpStatus.OK);
    }

    @PutMapping(path = "/jobs/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<JobOutputDTO>> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobInputDTO jobInputDTO) {
        JobOutputDTO jobOutputDTO = jobService.updateJob(id, jobInputDTO);
        return new ResponseEntity<>(ApiResponse.success(jobOutputDTO), HttpStatus.OK);
    }

    @DeleteMapping(path = "/jobs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return new ResponseEntity<>(ApiResponse.success(null), HttpStatus.OK);
    }
}
