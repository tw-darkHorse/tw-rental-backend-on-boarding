package rental.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.template.UriUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import rental.presentation.dto.response.common.ErrorResponse;
import rental.presentation.exception.InternalServerException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


@Configuration
@Slf4j
public class FeignClientConfig {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public ErrorDecoder feignErrorHandler() {
        final ErrorDecoder errorDecoder = new ErrorDecoder.Default();

        return (String methodKey, Response response) -> {
            if (Objects.nonNull(HttpStatus.resolve(response.status()))) {
                if (HttpStatus.resolve(response.status()).is2xxSuccessful()) {
                    return errorDecoder.decode(methodKey, response);
                } else if (HttpStatus.resolve(response.status()).is4xxClientError()) {
                    ErrorResponse errorResponse = new ErrorResponse();
                    try {
                        errorResponse = objectMapper.readValue(
                                IOUtils.toString(response.body().asInputStream(),
                                        String.valueOf(StandardCharsets.UTF_8)),
                                ErrorResponse.class);
                        log.info(String.valueOf(errorResponse));
                    } catch (IOException ex) {
                        log.error("read internal response body exception. {}", ex.toString());
                    }
                    return new InternalServerException(response.status(),
                            errorResponse.getCode(), errorResponse.getMessage());
                }
            }
            return new InternalServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "INTERNAL_SERVER_ERROR", UriUtils.decode(response.request().url(), StandardCharsets.UTF_8));
        };
    }
}
