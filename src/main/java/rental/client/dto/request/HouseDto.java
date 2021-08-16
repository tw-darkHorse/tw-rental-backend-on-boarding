package rental.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rental.domain.model.enums.HouseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseDto {
    private Long id;

    private String name;

    private String location;

    private BigDecimal price;

    private LocalDateTime establishedTime;

    private HouseStatus status;

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

}
