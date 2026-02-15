package vn.unigap.api.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.unigap.api.entity.JobApplicationEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApplicationOutputDTO {
    private Long id;
    private JobOutputDTO job;
    private ResumeOutputDTO resume;
    private String status;
    private Date appliedAt;
    private Date createdAt;
    private Date updatedAt;

    public static JobApplicationOutputDTO fromEntity(JobApplicationEntity entity) {
        return JobApplicationOutputDTO.builder()
                .id(entity.getId())
                .job(entity.getJob() != null ?
                        JobOutputDTO.fromEntity(entity.getJob()) : null)
                .resume(entity.getResume() != null ?
                        ResumeOutputDTO.fromEntity(entity.getResume()) : null)
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .appliedAt(entity.getAppliedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
