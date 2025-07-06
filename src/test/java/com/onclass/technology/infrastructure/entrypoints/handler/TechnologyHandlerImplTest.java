package com.onclass.technology.infrastructure.entrypoints.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onclass.technology.domain.api.TechnologyServicePort;
import com.onclass.technology.domain.enums.TechnicalMessage;
import com.onclass.technology.domain.exceptions.ParamRequiredMissingException;
import com.onclass.technology.domain.exceptions.TechnicalException;
import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.infrastructure.entrypoints.RouterRest;
import com.onclass.technology.infrastructure.entrypoints.dto.request.AssignTechnologiesDTO;
import com.onclass.technology.infrastructure.entrypoints.dto.request.CreateTechnologyDTO;
import com.onclass.technology.infrastructure.entrypoints.mapper.TechnologyMapper;
import com.onclass.technology.infrastructure.entrypoints.util.Constants;
import com.onclass.technology.infrastructure.entrypoints.util.ResponseDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, TechnologyHandlerImpl.class})
@WebFluxTest
public class TechnologyHandlerImplTest {

    @MockitoBean
    private TechnologyServicePort technologyServicePort;
    @MockitoBean
    private TechnologyMapper technologyMapper;

	@Autowired
    private WebTestClient webClient;

	private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
		objectMapper = new ObjectMapper();
    }

	@Nested
    @DisplayName("POST /technology")
    class createTechnologyTests {
        private final String pathTest ="/technology";

        static Stream<Arguments> exceptionAndStatusProvider() {
            return Stream.of(
                Arguments.of(new ParamRequiredMissingException(TechnicalMessage.MISSING_REQUIRED_PARAM), HttpStatus.CONFLICT.value()),
                Arguments.of(new TechnicalException(TechnicalMessage.UNSUPPORTED_OPERATION), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                Arguments.of(new RuntimeException(Constants.UNEXPECTED_ERROR), HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }

        @Test
        void testCreateTechnologySuccessful() throws Exception {
            CreateTechnologyDTO createTechnologyDTO = CreateTechnologyDTO.builder()
	            .description("backend")
                .name("Java")
                .build();
            String jsonBody = objectMapper.writeValueAsString(createTechnologyDTO);
            Technology technology = new Technology(1L, "Java", "Backend");
			ResponseDTO response = new ResponseDTO(TechnicalMessage.TECHNOLOGY_CREATED.getMessage());

            when(technologyServicePort.registerTechnology(any())).thenReturn(Mono.just(technology));
            when(technologyMapper.toTechnology(any())).thenReturn(technology);

            webClient.post()
                    .uri(pathTest)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(ResponseDTO.class)
                    .value(bodyResponse -> {
                                Assertions.assertThat(bodyResponse).isEqualTo(response);
                            }
                    );
        }

        @ParameterizedTest
        @MethodSource("exceptionAndStatusProvider")
        void testCreateTechnologyErrorCases(Exception exception, Integer statusCodeRs) throws Exception {
            CreateTechnologyDTO createTechnologyDTO = CreateTechnologyDTO.builder()
	            .description("backend")
                .name("Java")
                .build();
            String jsonBody = objectMapper.writeValueAsString(createTechnologyDTO);
            Technology technology = new Technology(1L, "Java", "Backend");

            when(technologyServicePort.registerTechnology(any())).thenReturn(Mono.error(exception));
            when(technologyMapper.toTechnology(any())).thenReturn(technology);

            webClient.post()
                    .uri(pathTest)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isEqualTo(statusCodeRs);
        }
    }

    @Nested
    @DisplayName("POST /technology/assign")
    class assignTechnologiesTests {
        private final String pathTest ="/technology/assign";

        static Stream<Arguments> exceptionAndStatusProvider() {
            return Stream.of(
                Arguments.of(new ParamRequiredMissingException(TechnicalMessage.MISSING_REQUIRED_PARAM), HttpStatus.CONFLICT.value()),
                Arguments.of(new TechnicalException(TechnicalMessage.UNSUPPORTED_OPERATION), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                Arguments.of(new RuntimeException(Constants.UNEXPECTED_ERROR), HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }

        @Test
        void testAssignTechnologiesSuccessful() throws Exception {
            AssignTechnologiesDTO assignTechnologiesDTO = AssignTechnologiesDTO.builder()
                .capacityID(1L)
                .technologiesIds(List.of(1L, 2L))
                .build();
            String jsonBody = objectMapper.writeValueAsString(assignTechnologiesDTO);
			ResponseDTO response = new ResponseDTO(TechnicalMessage.TECHNOLOGIES_ASSIGN_OK.getMessage());

            when(technologyServicePort.assignTechnologies(anyLong(), anyList())).thenReturn(Mono.empty());

            webClient.post()
                    .uri(pathTest)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(ResponseDTO.class)
                    .value(bodyResponse -> {
                                Assertions.assertThat(bodyResponse).isEqualTo(response);
                            }
                    );
        }

        @ParameterizedTest
        @MethodSource("exceptionAndStatusProvider")
        void testAssignTechnologiesErrorCases( Exception exception, Integer statusCodeRs) throws Exception {
            AssignTechnologiesDTO assignTechnologiesDTO = AssignTechnologiesDTO.builder()
                .capacityID(1L)
                .technologiesIds(List.of(1L, 2L))
                .build();
            String jsonBody = objectMapper.writeValueAsString(assignTechnologiesDTO);

            when(technologyServicePort.assignTechnologies(anyLong(), anyList())).thenReturn(Mono.error(exception));

            webClient.post()
                    .uri(pathTest)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isEqualTo(statusCodeRs);
        }
    }

/*
    @Test
    void create_shouldReturnCreatedAndValidBody() {
        // Arrange
        TechnologyDto dto = new TechnologyDto();
        dto.setName("React");

        List<TechnologyDto> dtoList = List.of(dto);
        Technology techDomain = new Technology();
        techDomain.setName("React");

        List<Technology> domainList = List.of(techDomain);

        TechnologyResponse responseDto = new TechnologyResponse();
        responseDto.setName("React");

        when(technologyValidationDto.validateNoDuplicateNames(dtoList))
                .thenReturn(Mono.empty());

        when(technologyValidationDto.validateFieldNotNullOrBlank(dto))
                .thenReturn(Mono.empty());

        when(technologyMapper.toTechnology(dto))
                .thenReturn(techDomain);
        when(technologyServicePort.save(domainList))
                .thenReturn(Mono.just(domainList));
        when(technologyMapperResponse.toTechnologyResponse(techDomain))
                .thenReturn(responseDto);

        when(applyErrorHandler.applyErrorHandling(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        webClient.post()
                .uri(Constants.PATH_POST_TECHNOLOGY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dtoList))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.TECHNOLOGY_CREATED.getMessage())
                .jsonPath("$.data").isArray()
                .jsonPath("$.data.length()").isEqualTo(1)
                .jsonPath("$.data[0].name").isEqualTo("React");
    }

    @Test
    void getTechnologiesByIds_shouldReturnOkAndValidBody() {
        // Arrange
        List<Long> ids = List.of(10L, 20L);
        var techDomain = new Technology();
        when(technologyServicePort.getTechnologiesByIds(ids))
                .thenReturn(Mono.just(List.of(techDomain)));

        TechnologyResponse techResponse = new TechnologyResponse();
        techResponse.setName("Spring");
        when(technologyMapperResponse.toTechnologyResponse(techDomain))
                .thenReturn(techResponse);
        when(applyErrorHandler.applyErrorHandling(any(Mono.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // Act & Assert
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(Constants.PATH_GET_TECHNOLOGIES_BY_IDS)
                        .queryParam("ids", "10,20")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.TECHNOLOGIES_FOUND.getCode())
                .jsonPath("$.data").isArray()
                .jsonPath("$.data.length()").isEqualTo(1)
                .jsonPath("$.data[0].name").isEqualTo("Spring");
    }

 */
}
