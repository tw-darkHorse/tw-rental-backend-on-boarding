package rental.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rental.client.HouseManagementServiceClient;
import rental.domain.model.House;
import rental.domain.repository.HouseRepository;
import rental.infrastructure.mapper.ModelToClientDtoMapper;
import rental.presentation.dto.CreateHouseRequest;
import rental.presentation.exception.InternalServerException;
import rental.presentation.exception.NotFoundException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HouseApplicationService {
    private final HouseRepository houseRepository;

    private final HouseManagementServiceClient houseManagementServiceClient;

    public Page<House> queryAllHouses(Pageable pageable) {
        return houseRepository.queryAllHouses(pageable);
    }

    public House findHouseById(long id) {
        Optional<House> house = houseRepository.findById(id);
        return house.orElseThrow(() -> new NotFoundException("NOT_FOUND", "can't find house with house id " + id));
    }

    public void saveHouse(CreateHouseRequest createHouseRequest) {
        House house = House.init(createHouseRequest.getName(), createHouseRequest.getLocation(),
                createHouseRequest.getPrice(), createHouseRequest.getEstablishedTime());
        House savedHouse = houseRepository.save(house);
        try {
            houseManagementServiceClient.saveHouse(ModelToClientDtoMapper.INSTANCE.mapToDto(savedHouse));
        } catch (InternalServerException ex) {
            this.houseRepository.deleteById(savedHouse.getId());
            throw ex;
        }
    }
}
