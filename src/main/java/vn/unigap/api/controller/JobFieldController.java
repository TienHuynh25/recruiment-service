package vn.unigap.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.unigap.api.dto.out.JobFieldOutputDTO;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.service.JobFieldService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Job Fields", description = "Job field/category metadata APIs (read-only)")
public class JobFieldController {

    @Autowired
    private JobFieldService jobFieldService;

    @GetMapping(path = "/job-fields", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<JobFieldOutputDTO>> getAllJobFields(Pageable pageable) {
        PageResponse<JobFieldOutputDTO> response = jobFieldService.getAllJobFields(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/job-fields/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobFieldOutputDTO> getJobFieldById(@PathVariable Long id) {
        JobFieldOutputDTO dto = jobFieldService.getJobFieldById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
