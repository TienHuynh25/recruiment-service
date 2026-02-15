package vn.unigap.api.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.unigap.api.entity.SeekerEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeekerOutputDTO {
    private Long id;
    private String name;
    private String email;
    private String birthday;
    private String address;
    private JobProvinceOutputDTO province;
    private Date createdAt;
    private Date updatedAt;

    public static SeekerOutputDTO fromEntity(SeekerEntity entity) {
        return SeekerOutputDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .birthday(entity.getBirthday())
                .address(entity.getAddress())
                .province(entity.getProvince() != null ?
                        JobProvinceOutputDTO.fromEntity(entity.getProvince()) : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
