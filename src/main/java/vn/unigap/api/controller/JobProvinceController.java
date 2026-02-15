package vn.unigap.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.unigap.api.dto.out.JobProvinceOutputDTO;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.service.JobProvinceService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Provinces", description = "Province/location metadata APIs (read-only)")
public class JobProvinceController {

    @Autowired
    private JobProvinceService jobProvinceService;

    @GetMapping(path = "/provinces", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<JobProvinceOutputDTO>> getAllProvinces(Pageable pageable) {
        PageResponse<JobProvinceOutputDTO> response = jobProvinceService.getAllProvinces(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/provinces/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobProvinceOutputDTO> getProvinceById(@PathVariable Long id) {
        JobProvinceOutputDTO dto = jobProvinceService.getProvinceById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
