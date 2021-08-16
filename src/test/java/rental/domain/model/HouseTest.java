package rental.domain.model;

import org.junit.Test;
import rental.domain.model.enums.HouseStatus;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HouseTest {
    @Test
    public void should_creat_house_given_valid_house_info() {
        //given
        String name = "house-1";
        String location = "shenzhen";
        BigDecimal price = BigDecimal.valueOf(7000);
        LocalDateTime establishedTime = LocalDateTime.now();

        //when
        House house = House.init(name, location, price, establishedTime);

        //then
        assertEquals(name, house.getName());
        assertEquals(location, house.getLocation());
        assertEquals(price, house.getPrice());
        assertEquals(establishedTime, house.getEstablishedTime());
        assertEquals(HouseStatus.PENDING, house.getStatus());
    }
}
