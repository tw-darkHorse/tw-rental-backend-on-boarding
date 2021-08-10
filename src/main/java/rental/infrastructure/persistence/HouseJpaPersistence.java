package rental.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import rental.infrastructure.dataentity.HouseEntity;

public interface HouseJpaPersistence extends JpaRepository<HouseEntity, Long> {
}
