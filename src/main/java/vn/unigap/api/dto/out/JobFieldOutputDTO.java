package vn.unigap.api.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.unigap.api.entity.JobFieldEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobFieldOutputDTO {
    private Long id;
    private String name;
    private String slug;
    private Date createdAt;
    private Date updatedAt;

    public static JobFieldOutputDTO fromEntity(JobFieldEntity entity) {
        return JobFieldOutputDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
