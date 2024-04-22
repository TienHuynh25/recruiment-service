package vn.unigap.api.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployerInputDTO {
//    @NotEmpty(message = Common.VALIDATE_EMAIL_NOT_EMPTY)
//    @Email(message = Common.VALIDATE_EMAIL_INVALID)
//    @Size(min = 3, max = 255, message = Common.VALIDATE_EMAIL_INVALID)
    private String email;

//    @NotEmpty(message = Common.VALIDATE_NAME_NOT_EMPTY)
//    @Size(min = 3, max = 255, message = Common.VALIDATE_NAME_INVALID)
    private String name;

//    @NotEmpty(message = Common.VALIDATE_PROVINCE_NOT_EMPTY)
//    @province(min = 1, max = 64, message = Common.VALIDATE_PROVINCE_INVALID)
    private int province;


//    @Size(min = 3, max = 500, message = Common.VALIDATE_DESCRIPTION_INVALID)
    private String description;

    }
