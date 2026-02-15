package vn.unigap.api.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.unigap.api.common.Constants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeekerInputDTO {
    @NotEmpty(message = Constants.VALIDATE_NAME_NOT_EMPTY)
    @Size(min = 3, max = 255, message = Constants.VALIDATE_NAME_SIZE)
    private String name;

    @NotEmpty(message = Constants.VALIDATE_EMAIL_NOT_EMPTY)
    @Email(message = Constants.VALIDATE_EMAIL_INVALID)
    @Size(min = 3, max = 255, message = Constants.VALIDATE_EMAIL_SIZE)
    private String email;

    private String birthday;

    @Size(min = 3, max = 500, message = Constants.VALIDATE_ADDRESS_SIZE)
    private String address;

    @NotNull(message = Constants.VALIDATE_PROVINCE_NOT_EMPTY)
    private Long provinceId;
}
