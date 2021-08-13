package rental.application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import rental.domain.model.House;
import rental.domain.repository.HouseRepository;
import rental.presentation.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class HouseApplicationServiceTest {
    @InjectMocks
    private HouseApplicationService applicationService;

    @Mock
    private HouseRepository repository;

    @Test
    public void should_get_all_houses() {
        // given
        List<House> houseList = Arrays.asList(
                House.builder().id(1L).name("name-1").build(),
                House.builder().id(2L).name("name-2").build());
        Page<House> housePage = new PageImpl<>(houseList);
        when(repository.queryAllHouses(any())).thenReturn(housePage);
        PageRequest pageable = PageRequest.of(0, 20);

        // when
        Page<House> result = applicationService.queryAllHouses(pageable);

        // then
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void should_get_house_info_given_valid_id() {
        //given
        House house = House.builder().id(1L).name("house-1").build();
        when(repository.findById(1L)).thenReturn(Optional.of(house));

        //when
        House expectHouse = applicationService.findHouseById(1L);

        //then
        assertEquals(expectHouse.getName(), house.getName());
    }

    @Test
    public void should_throw_not_found_exception_given_invalid_house_id() {
        //given
        House house = House.builder().id(1L).name("house-1").build();

        //when
        Throwable throwable = catchThrowable(() -> applicationService.findHouseById(house.getId() + 10101));

        //then
        assertThat(throwable)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("can't find house with house id " + (house.getId() + 10101));
    }
}
