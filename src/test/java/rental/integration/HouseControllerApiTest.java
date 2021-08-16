package rental.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rental.RentalServiceApplication;
import rental.config.BaseIntegrationTest;
import rental.config.WireMockConfig;
import rental.config.client.HouseManagementServiceClientMocks;
import rental.infrastructure.dataentity.HouseEntity;
import rental.infrastructure.persistence.HouseJpaPersistence;
import rental.presentation.dto.CreateHouseRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RentalServiceApplication.class
)
@ActiveProfiles("test")
@EnableConfigurationProperties
@ContextConfiguration(classes = { WireMockConfig.class })
public class HouseControllerApiTest extends BaseIntegrationTest {
    @Autowired
    private ApplicationContext applicationContext;

    private HouseJpaPersistence persistence;

    @Autowired
    private WireMockServer mockHouseManagementClientService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    @Test
    public void should_save_house_and_call_remote_client() throws Exception {
        //given
        String name = "house-1";
        String location = "shenzhen";
        BigDecimal price = BigDecimal.valueOf(7000);
        CreateHouseRequest createHouseRequest = CreateHouseRequest.builder()
                .name(name)
                .location(location)
                .price(price)
                .build();
        HouseManagementServiceClientMocks.setUpSuccessMockResponse(mockHouseManagementClientService);

        //when
        //then
        given()
                .body(objectMapper.writeValueAsString(createHouseRequest))
                .when()
                .post("/houses")
                .then()
                .status(HttpStatus.CREATED);
    }

    @Test
    public void should_throw_error_when_given_house_info_missing_filed() throws Exception {
        //given
        String name = "house-1";
        BigDecimal price = BigDecimal.valueOf(7000);
        CreateHouseRequest createHouseRequest = CreateHouseRequest.builder()
                .name(name)
                .price(price)
                .build();

        //when
        //then
        given()
                .body(objectMapper.writeValueAsString(createHouseRequest))
                .when()
                .post("/houses")
                .then()
                .status(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void should_throw_exception_when_remote_client_failed() throws Exception {
        //given
        String name = "house-1";
        String location = "shenzhen";
        BigDecimal price = BigDecimal.valueOf(7000);
        CreateHouseRequest createHouseRequest = CreateHouseRequest.builder()
                .name(name)
                .location(location)
                .price(price)
                .build();
        HouseManagementServiceClientMocks.setUpFailMockResponse(mockHouseManagementClientService);

        //when
        //then
        given()
                .body(objectMapper.writeValueAsString(createHouseRequest))
                .when()
                .post("/houses")
                .then()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("status", is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .body("code", is("INTERNAL_SERVER_EXCEPTION"))
                .body("message", is("InternalServeException"));
    }
}
