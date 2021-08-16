package rental.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import rental.application.HouseApplicationService;
import rental.domain.model.House;
import rental.presentation.dto.CreateHouseRequest;
import rental.presentation.exception.InternalServerException;
import rental.presentation.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HouseControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private HouseApplicationService applicationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void should_get_all_houses() throws Exception {
        // given
        List<House> houseList = Arrays.asList(
                House.builder().id(1L).name("name-1").build(),
                House.builder().id(2L).name("name-2").build());
        Page<House> housePage = new PageImpl<>(houseList);
        when(applicationService.queryAllHouses(any())).thenReturn(housePage);

        // when
        mvc.perform(get("/houses").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    public void should_get_house_info_given_valid_house_id() throws Exception {
        //given
        House house = House.builder().id(1L).name("house-1").build();
        when(applicationService.findHouseById(1L)).thenReturn(house);

        //when
        //then
        mvc.perform(get("/houses/" + house.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(house.getName()));
    }

    @Test
    public void should_get_error_message_when_find_house_given_invalid_house_id() throws Exception {
        //given
        House house = House.builder().id(1L).name("house-1").build();
        long wrongId = house.getId() + 111L;
        when(applicationService.findHouseById(wrongId)).thenThrow(
                new NotFoundException("NOT_FOUND", "can't find house with house id " + wrongId));

        //when
        //then
        mvc.perform(get("/houses/" + wrongId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("can't find house with house id " + wrongId));

    }

    @Test
    public void should_save_house_given_valid_house_info() throws Exception {
        //given
        String name = "house-1";
        String location = "shenzhen";
        BigDecimal price = BigDecimal.valueOf(7000);
        CreateHouseRequest createHouseRequest = CreateHouseRequest.builder()
                .name(name)
                .location(location)
                .price(price)
                .build();
        doNothing().when(applicationService).saveHouse(any());
        //when
        //then
        mvc.perform(
                post("/houses")
                        .content(objectMapper.writeValueAsString(createHouseRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(applicationService, times(1)).saveHouse(any());
    }

    @Test
    public void should_throw_error_when_miss_authenticate_filed_in_save_house_info() throws Exception {
        //given
        String name = "house-1";
        BigDecimal price = BigDecimal.valueOf(7000);
        CreateHouseRequest createHouseRequest = CreateHouseRequest.builder()
                .name(name)
                .price(price)
                .build();

        //when
        //then
        mvc.perform(post("/houses")
                .content(objectMapper.writeValueAsString(createHouseRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    public void should_throw_exception_when_application_server_failed() throws Exception {
        //given
        String name = "house-1";
        String location = "shenzhen";
        BigDecimal price = BigDecimal.valueOf(7000);
        CreateHouseRequest createHouseRequest = CreateHouseRequest.builder()
                .name(name)
                .location(location)
                .price(price)
                .build();
        doThrow(new InternalServerException(500, "INTERNAL_SERVER_EXCEPTION", "InternalServeException"))
                .when(applicationService).saveHouse(any());
        //when
        //then
        mvc.perform(
                post("/houses")
                        .content(objectMapper.writeValueAsString(createHouseRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("500"))
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_EXCEPTION"));
        verify(applicationService, times(1)).saveHouse(any());
    }
}
