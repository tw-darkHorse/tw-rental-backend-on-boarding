package rental.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rental.client.dto.request.HouseDto;
import rental.config.FeignClientConfig;

@FeignClient(name = "${application.services.house-management.name}",
        url = "${application.services.house-management.url}",
        configuration = FeignClientConfig.class)
public interface HouseManagementServiceClient {
    @PostMapping("/houses")
    void saveHouse(@RequestBody HouseDto houseDto);
}
