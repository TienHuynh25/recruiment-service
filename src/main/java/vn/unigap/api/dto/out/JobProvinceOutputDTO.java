package vn.unigap.api.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.unigap.api.entity.JobProvinceEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobProvinceOutputDTO {
    private Long id;
    private String name;
    private String slug;
    private String code;
    private Date createdAt;
    private Date updatedAt;

    public static JobProvinceOutputDTO fromEntity(JobProvinceEntity entity) {
        return JobProvinceOutputDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .code(entity.getCode())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
