package vn.unigap.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.unigap.api.dto.in.SeekerInputDTO;
import vn.unigap.api.dto.out.SeekerOutputDTO;
import vn.unigap.api.dto.response.ApiResponse;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.service.SeekerService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Seekers", description = "Job seeker management APIs")
public class SeekerController {

    @Autowired
    private SeekerService seekerService;

    @PostMapping(path = "/seekers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<SeekerOutputDTO>> createSeeker(@Valid @RequestBody SeekerInputDTO seekerInputDTO) {
        SeekerOutputDTO seekerOutputDTO = seekerService.createSeeker(seekerInputDTO);
        return new ResponseEntity<>(ApiResponse.created(seekerOutputDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "/seekers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<SeekerOutputDTO>> getAllSeekers(Pageable pageable) {
        PageResponse<SeekerOutputDTO> response = seekerService.getAllSeekers(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/seekers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<SeekerOutputDTO>> getSeekerById(@PathVariable Long id) {
        SeekerOutputDTO seekerOutputDTO = seekerService.getSeekerById(id);
        return new ResponseEntity<>(ApiResponse.success(seekerOutputDTO), HttpStatus.OK);
    }

    @PutMapping(path = "/seekers/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<SeekerOutputDTO>> updateSeeker(
            @PathVariable Long id,
            @Valid @RequestBody SeekerInputDTO seekerInputDTO) {
        SeekerOutputDTO seekerOutputDTO = seekerService.updateSeeker(id, seekerInputDTO);
        return new ResponseEntity<>(ApiResponse.success(seekerOutputDTO), HttpStatus.OK);
    }

    @DeleteMapping(path = "/seekers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSeeker(@PathVariable Long id) {
        seekerService.deleteSeeker(id);
        return new ResponseEntity<>(ApiResponse.success(null), HttpStatus.OK);
    }
}
