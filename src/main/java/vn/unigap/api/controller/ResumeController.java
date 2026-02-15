package vn.unigap.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.unigap.api.dto.in.ResumeInputDTO;
import vn.unigap.api.dto.out.ResumeOutputDTO;
import vn.unigap.api.dto.response.ApiResponse;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.service.ResumeService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Resumes", description = "Resume management APIs for job seekers")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping(path = "/resumes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ResumeOutputDTO>> createResume(@Valid @RequestBody ResumeInputDTO resumeInputDTO) {
        ResumeOutputDTO resumeOutputDTO = resumeService.createResume(resumeInputDTO);
        return new ResponseEntity<>(ApiResponse.created(resumeOutputDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "/resumes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<ResumeOutputDTO>> getAllResumes(
            @RequestParam(required = false) Long seekerId,
            @RequestParam(required = false) Long fieldId,
            @RequestParam(required = false) Long provinceId,
            Pageable pageable) {
        PageResponse<ResumeOutputDTO> response = resumeService.getAllResumes(seekerId, fieldId, provinceId, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/resumes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ResumeOutputDTO>> getResumeById(@PathVariable Long id) {
        ResumeOutputDTO resumeOutputDTO = resumeService.getResumeById(id);
        return new ResponseEntity<>(ApiResponse.success(resumeOutputDTO), HttpStatus.OK);
    }

    @PutMapping(path = "/resumes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ResumeOutputDTO>> updateResume(
            @PathVariable Long id,
            @Valid @RequestBody ResumeInputDTO resumeInputDTO) {
        ResumeOutputDTO resumeOutputDTO = resumeService.updateResume(id, resumeInputDTO);
        return new ResponseEntity<>(ApiResponse.success(resumeOutputDTO), HttpStatus.OK);
    }

    @DeleteMapping(path = "/resumes/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return new ResponseEntity<>(ApiResponse.success(null), HttpStatus.OK);
    }
}
