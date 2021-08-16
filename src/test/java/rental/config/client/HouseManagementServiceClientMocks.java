package rental.config.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import rental.presentation.dto.response.common.ErrorResponse;

import java.io.IOException;

public class HouseManagementServiceClientMocks {
    public static void setUpSuccessMockResponse(WireMockServer mockServer) {
        mockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/houses"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
    }

    public static void setUpFailMockResponse(WireMockServer mockServer) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(500, "INTERNAL_SERVER_EXCEPTION", "InternalServeException");
        ObjectMapper objectMapper = new ObjectMapper();
        mockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/houses"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatusMessage("message")
                        .withBody(objectMapper.writeValueAsString(errorResponse))
                ));
    }
}
