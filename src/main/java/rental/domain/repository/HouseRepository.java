package rental.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rental.domain.model.House;

import java.util.Optional;

public interface HouseRepository {
    Page<House> queryAllHouses(Pageable pageable);

    Optional<House> findById(Long id);

    House save(House house);

    void deleteById(Long id);
}
