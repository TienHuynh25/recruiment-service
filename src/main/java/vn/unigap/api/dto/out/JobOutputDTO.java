package vn.unigap.api.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.unigap.api.entity.JobEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobOutputDTO {
    private Long id;
    private EmployerOutputDTO employer;
    private String title;
    private Integer quantity;
    private String description;
    private List<JobFieldOutputDTO> fields;
    private List<JobProvinceOutputDTO> provinces;
    private BigDecimal salary;
    private Date expiredAt;
    private Date createdAt;
    private Date updatedAt;

    public static JobOutputDTO fromEntity(JobEntity entity) {
        return JobOutputDTO.builder()
                .id(entity.getId())
                .employer(entity.getEmployer() != null ?
                        EmployerOutputDTO.fromEntity(entity.getEmployer()) : null)
                .title(entity.getTitle())
                .quantity(entity.getQuantity())
                .description(entity.getDescription())
                .fields(entity.getFields() != null ?
                        entity.getFields().stream()
                                .map(JobFieldOutputDTO::fromEntity)
                                .collect(Collectors.toList()) : null)
                .provinces(entity.getProvinces() != null ?
                        entity.getProvinces().stream()
                                .map(JobProvinceOutputDTO::fromEntity)
                                .collect(Collectors.toList()) : null)
                .salary(entity.getSalary())
                .expiredAt(entity.getExpiredAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
