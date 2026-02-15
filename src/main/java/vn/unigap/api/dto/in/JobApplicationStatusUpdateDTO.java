package vn.unigap.api.dto.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.unigap.api.entity.JobApplicationEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApplicationStatusUpdateDTO {
    @NotNull(message = "Status is required")
    private JobApplicationEntity.ApplicationStatus status;
}
