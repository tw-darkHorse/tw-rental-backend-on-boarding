package rental.infrastructure.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import rental.domain.model.House;
import rental.infrastructure.dataentity.HouseEntity;
import rental.infrastructure.persistence.HouseJpaPersistence;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class HouseRepositoryImplTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private HouseJpaPersistence persistence;

    private HouseRepositoryImpl repository;

    @Before
    public void setUp() {
        repository = new HouseRepositoryImpl(persistence);
    }

    @Test
    public void should_find_2_houses_with_page() {
        // given
        entityManager.persistAndFlush(HouseEntity.builder().name("house-1").build());
        entityManager.persistAndFlush(HouseEntity.builder().name("house-2").build());
        PageRequest pageable = PageRequest.of(0, 20);

        // when
        Page<House> result = this.repository.queryAllHouses(pageable);

        // then
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void should_find_21_houses_with_page() {
        // given
        for (int index = 1; index <= 21; index++) {
            entityManager.persistAndFlush(HouseEntity.builder().name("house-" + index).build());
        }
        PageRequest pageable = PageRequest.of(0, 20);

        // when
        Page<House> result = this.repository.queryAllHouses(pageable);

        // then
        assertEquals(21, result.getTotalElements());
        assertEquals(20, result.getContent().size());
        assertEquals(2, result.getTotalPages());
    }

    @Test
    public void should_find_0_houses_with_page() {
        // given
        PageRequest pageable = PageRequest.of(0, 20);

        // when
        Page<House> result = this.repository.queryAllHouses(pageable);

        // then
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());
    }

    @Test
    public void should_get_house_info_when_id_exist() {
        //given
        HouseEntity houseEntity = HouseEntity.builder().name("house-1").build();
        entityManager.persistAndFlush(houseEntity);
        Long id = houseEntity.getId();

        //when
        Optional<House> actualHouse = this.repository.findById(id);

        //then
        assertTrue(actualHouse.isPresent());
        assertEquals("house-1", actualHouse.get().getName());
    }

    @Test
    public void should_return_empty_option_given_house_id_not_exist() {
        //given
        HouseEntity houseEntity = HouseEntity.builder().name("house-1").build();
        entityManager.persistAndFlush(houseEntity);
        Long id = houseEntity.getId();

        //when
        Optional<House> actualHouse = this.repository.findById(id + 101010);

        //then
        assertFalse(actualHouse.isPresent());
    }
}
