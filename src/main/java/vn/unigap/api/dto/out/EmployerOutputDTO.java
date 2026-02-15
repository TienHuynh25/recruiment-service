package vn.unigap.api.dto.out;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.unigap.api.entity.EmployerEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployerOutputDTO {
    private Long id;
    private String email;
    private String name;
    private JobProvinceOutputDTO province;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    public static EmployerOutputDTO fromEntity(EmployerEntity employerEntity) {
        return EmployerOutputDTO.builder()
            .id(employerEntity.getId())
            .email(employerEntity.getEmail())
            .name(employerEntity.getName())
            .province(employerEntity.getProvince() != null ?
                JobProvinceOutputDTO.fromEntity(employerEntity.getProvince()) : null)
            .description(employerEntity.getDescription())
            .createdAt(employerEntity.getCreatedAt())
            .updatedAt(employerEntity.getUpdatedAt())
            .build();
    }


}
