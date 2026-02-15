package vn.unigap.api.dto.in;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.unigap.api.common.Constants;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobInputDTO {
    @NotNull(message = "Employer ID is required")
    private Long employerId;

    @NotEmpty(message = Constants.VALIDATE_TITLE_NOT_EMPTY)
    @Size(min = 3, max = 255, message = Constants.VALIDATE_TITLE_SIZE)
    private String title;

    @NotNull(message = Constants.VALIDATE_QUANTITY_NOT_EMPTY)
    @Min(value = 1, message = Constants.VALIDATE_QUANTITY_POSITIVE)
    private Integer quantity;

    @Size(min = 3, max = 2000, message = Constants.VALIDATE_DESCRIPTION_SIZE)
    private String description;

    @NotEmpty(message = Constants.VALIDATE_FIELDS_NOT_EMPTY)
    private List<Long> fieldIds;

    @NotEmpty(message = Constants.VALIDATE_PROVINCES_NOT_EMPTY)
    private List<Long> provinceIds;

    @NotNull(message = Constants.VALIDATE_SALARY_NOT_EMPTY)
    @DecimalMin(value = "0.0", inclusive = false, message = Constants.VALIDATE_SALARY_POSITIVE)
    private BigDecimal salary;

    @NotNull(message = Constants.VALIDATE_EXPIRED_AT_NOT_EMPTY)
    @Future(message = Constants.VALIDATE_EXPIRED_AT_FUTURE)
    private Date expiredAt;
}
