package vn.unigap.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int errorCode;
    private int statusCode;
    private String message;
    private T object;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .errorCode(0)
                .statusCode(200)
                .message("Success")
                .object(data)
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .errorCode(0)
                .statusCode(201)
                .message("Created successfully")
                .object(data)
                .build();
    }

    public static <T> ApiResponse<T> error(int statusCode, int errorCode, String message) {
        return ApiResponse.<T>builder()
                .errorCode(errorCode)
                .statusCode(statusCode)
                .message(message)
                .build();
    }
}
