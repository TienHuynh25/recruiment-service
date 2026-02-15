package vn.unigap.api.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.unigap.api.entity.ResumeEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeOutputDTO {
    private Long id;
    private SeekerOutputDTO seeker;
    private String careerObj;
    private String title;
    private BigDecimal salary;
    private List<JobFieldOutputDTO> fields;
    private List<JobProvinceOutputDTO> provinces;
    private Date createdAt;
    private Date updatedAt;

    public static ResumeOutputDTO fromEntity(ResumeEntity entity) {
        return ResumeOutputDTO.builder()
                .id(entity.getId())
                .seeker(entity.getSeeker() != null ?
                        SeekerOutputDTO.fromEntity(entity.getSeeker()) : null)
                .careerObj(entity.getCareerObj())
                .title(entity.getTitle())
                .salary(entity.getSalary())
                .fields(entity.getFields() != null ?
                        entity.getFields().stream()
                                .map(JobFieldOutputDTO::fromEntity)
                                .collect(Collectors.toList()) : null)
                .provinces(entity.getProvinces() != null ?
                        entity.getProvinces().stream()
                                .map(JobProvinceOutputDTO::fromEntity)
                                .collect(Collectors.toList()) : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
