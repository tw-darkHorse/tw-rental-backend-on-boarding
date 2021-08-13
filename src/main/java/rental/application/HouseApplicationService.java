package rental.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rental.domain.model.House;
import rental.domain.repository.HouseRepository;
import rental.presentation.exception.NotFoundException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HouseApplicationService {
    private final HouseRepository houseRepository;

    public Page<House> queryAllHouses(Pageable pageable) {
        return houseRepository.queryAllHouses(pageable);
    }

    public House findHouseById(long id) {
        Optional<House> house = houseRepository.findById(id);
        return house.orElseThrow(() -> new NotFoundException("NOT_FOUND", "can't find house with house id " + id));
    }
}
