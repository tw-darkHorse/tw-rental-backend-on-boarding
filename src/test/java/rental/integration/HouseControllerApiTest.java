package rental.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rental.RentalServiceApplication;
import rental.config.BaseIntegrationTest;
import rental.infrastructure.dataentity.HouseEntity;
import rental.infrastructure.persistence.HouseJpaPersistence;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RentalServiceApplication.class
)
public class HouseControllerApiTest extends BaseIntegrationTest {
    @Autowired
    private ApplicationContext applicationContext;

    private HouseJpaPersistence persistence;

    @Before
    public void setUp() {
        persistence = applicationContext.getBean(HouseJpaPersistence.class);
    }

    @Test
    public void should_get_all_houses() {
        // given
        persistence.saveAndFlush(HouseEntity.builder().name("house-1").build());
        persistence.saveAndFlush(HouseEntity.builder().name("house-2").build());

        // when
        given()
                .when()
                .get("/houses")
                .then()
                .statusCode(200)
                .body("totalElements", is(2))
                .body("content", hasSize(2));
    }

    @Test
    public void should_get_house_info_given_valid_house_id() {
        //given
        HouseEntity houseEntity = HouseEntity.builder().name("house-1").build();
        persistence.saveAndFlush(houseEntity);
        Long id = houseEntity.getId();

        //when
        given()
                .when()
                .get("/houses/" + id)
                .then()
                .status(HttpStatus.OK)
                .body("id", is(houseEntity.getId().intValue()))
                .body("name",is(houseEntity.getName()));
    }

    @Test
    public void should_throw_not_found_error_message_when_find_house_info_given_invalid_id() {
        //given
        HouseEntity houseEntity = HouseEntity.builder().name("house-1").build();
        persistence.saveAndFlush(houseEntity);
        long wrongId = houseEntity.getId() + 111L;

        //when
        given()
                .when()
                .get("/houses/" + wrongId)
                .then()
                .status(HttpStatus.NOT_FOUND)
                .body("status", is(HttpStatus.NOT_FOUND.value()))
                .body("code", is("NOT_FOUND"))
                .body("message", is("can't find house with house id " + wrongId));
    }
}
