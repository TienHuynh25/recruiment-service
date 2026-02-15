package vn.unigap.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.unigap.api.common.Constants;
import vn.unigap.api.dto.in.SeekerInputDTO;
import vn.unigap.api.dto.out.SeekerOutputDTO;
import vn.unigap.api.dto.response.PageResponse;
import vn.unigap.api.entity.JobProvinceEntity;
import vn.unigap.api.entity.SeekerEntity;
import vn.unigap.api.exception.DuplicateResourceException;
import vn.unigap.api.exception.ResourceNotFoundException;
import vn.unigap.api.repository.JobProvinceRepository;
import vn.unigap.api.repository.SeekerRepository;

import java.util.Date;

@Service
public class SeekerService {

    @Autowired
    private SeekerRepository seekerRepository;

    @Autowired
    private JobProvinceRepository jobProvinceRepository;

    public SeekerOutputDTO createSeeker(SeekerInputDTO seekerInputDTO) {
        // Check if email already exists
        if (seekerRepository.findByEmail(seekerInputDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException(Constants.DUPLICATE_EMAIL + seekerInputDTO.getEmail());
        }

        // Validate and get province
        JobProvinceEntity province = jobProvinceRepository.findById(seekerInputDTO.getProvinceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.JOB_PROVINCE_NOT_FOUND + seekerInputDTO.getProvinceId()));

        SeekerEntity seekerEntity = SeekerEntity.builder()
                .name(seekerInputDTO.getName())
                .email(seekerInputDTO.getEmail())
                .birthday(seekerInputDTO.getBirthday())
                .address(seekerInputDTO.getAddress())
                .province(province)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        seekerRepository.save(seekerEntity);
        return SeekerOutputDTO.fromEntity(seekerEntity);
    }

    public PageResponse<SeekerOutputDTO> getAllSeekers(Pageable pageable) {
        Page<SeekerEntity> page = seekerRepository.findAll(pageable);
        Page<SeekerOutputDTO> dtoPage = page.map(SeekerOutputDTO::fromEntity);
        return PageResponse.fromPage(dtoPage);
    }

    public SeekerOutputDTO getSeekerById(Long id) {
        SeekerEntity seekerEntity = seekerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.SEEKER_NOT_FOUND + id));
        return SeekerOutputDTO.fromEntity(seekerEntity);
    }

    public SeekerOutputDTO updateSeeker(Long id, SeekerInputDTO seekerInputDTO) {
        SeekerEntity seeker = seekerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.SEEKER_NOT_FOUND + id));

        // Check if email is being changed and if new email already exists
        if (!seeker.getEmail().equals(seekerInputDTO.getEmail())) {
            if (seekerRepository.findByEmail(seekerInputDTO.getEmail()).isPresent()) {
                throw new DuplicateResourceException(Constants.DUPLICATE_EMAIL + seekerInputDTO.getEmail());
            }
        }

        // Validate and get province
        JobProvinceEntity province = jobProvinceRepository.findById(seekerInputDTO.getProvinceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Constants.JOB_PROVINCE_NOT_FOUND + seekerInputDTO.getProvinceId()));

        seeker.setName(seekerInputDTO.getName());
        seeker.setEmail(seekerInputDTO.getEmail());
        seeker.setBirthday(seekerInputDTO.getBirthday());
        seeker.setAddress(seekerInputDTO.getAddress());
        seeker.setProvince(province);
        seeker.setUpdatedAt(new Date());

        seekerRepository.save(seeker);
        return SeekerOutputDTO.fromEntity(seeker);
    }

    public void deleteSeeker(Long id) {
        if (!seekerRepository.existsById(id)) {
            throw new ResourceNotFoundException(Constants.SEEKER_NOT_FOUND + id);
        }
        seekerRepository.deleteById(id);
    }
}
