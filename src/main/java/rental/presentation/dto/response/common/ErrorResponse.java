package rental.presentation.dto.response.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Data
public class ErrorResponse {
    private Integer status;
    private String code;
    private String message;
}
