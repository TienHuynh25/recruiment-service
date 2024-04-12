package vn.unigap.api.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.unigap.api.dto.in.EmployerInputDTO;
import vn.unigap.api.dto.out.EmployerOutputDTO;
import vn.unigap.api.service.EmployerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployerController {

    @Autowired
    private EmployerService employerService;

    @PostMapping(path = "/employer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployerOutputDTO> createEmployer(@Valid @RequestBody EmployerInputDTO employerInputDTO) {
        EmployerOutputDTO employerOutputDTO = employerService.createEmployer(employerInputDTO);
        return new ResponseEntity<>(employerOutputDTO, HttpStatus.CREATED);
    }

    @GetMapping(path = "/employer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployerOutputDTO>> getAllEmployer(Pageable pageable) {
        List<EmployerOutputDTO> employerOutputDTOList = employerService.getAllEmployer(pageable);
        return new ResponseEntity<>(employerOutputDTOList, HttpStatus.OK);
    }

    @GetMapping(path = "/employer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployerOutputDTO> getEmployerById(@PathVariable Long id) {
        EmployerOutputDTO employerOutputDTO = employerService.getEmployerById(id);
        return new ResponseEntity<>(employerOutputDTO, HttpStatus.OK);
    }

    @PutMapping(path = "/employer/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployerOutputDTO> updateEmployerById(@PathVariable Long id, @Valid @RequestBody EmployerInputDTO employerInputDTO) {
        EmployerOutputDTO employerOutputDTO = employerService.updateEmployerById(id, employerInputDTO);
        return new ResponseEntity<>(employerOutputDTO, HttpStatus.OK);
    }

    @DeleteMapping(path = "/employer/{id}")
    public ResponseEntity<Void> deleteEmployerById(@PathVariable Long id) {
        employerService.deleteEmployerById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
