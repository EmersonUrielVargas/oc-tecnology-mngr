package com.onclass.technology.infrastructure.entrypoints.handler;

import com.onclass.technology.domain.api.TechnologyServicePort;
import com.onclass.technology.domain.enums.TechnicalMessage;
import com.onclass.technology.domain.exceptions.BusinessException;
import com.onclass.technology.domain.exceptions.TechnicalException;
import com.onclass.technology.domain.model.spi.CapacityItem;
import com.onclass.technology.infrastructure.entrypoints.dto.request.AssignTechnologiesDTO;
import com.onclass.technology.infrastructure.entrypoints.dto.request.CreateTechnologyDTO;
import com.onclass.technology.infrastructure.entrypoints.mapper.TechnologyMapper;
import com.onclass.technology.infrastructure.entrypoints.util.Constants;
import com.onclass.technology.infrastructure.entrypoints.util.ErrorDTO;
import com.onclass.technology.infrastructure.entrypoints.util.ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TechnologyHandlerImpl {

    private final TechnologyServicePort technologyServicePort;
    private final TechnologyMapper technologyMapper;

    public Mono<ServerResponse> createTechnology(ServerRequest request) {
        return request.bodyToMono(CreateTechnologyDTO.class)
                .flatMap(technologyDTO -> technologyServicePort.registerTechnology(technologyMapper.toTechnology(technologyDTO))
                        .doOnSuccess(savedTechnology -> log.info(Constants.TECHNOLOGY_CREATED_RS_OK))
                )
                .flatMap(savedTechnology -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(new ResponseDTO(TechnicalMessage.TECHNOLOGY_CREATED.getMessage())))
                .doOnError(ex -> log.error(Constants.TECHNOLOGY_ERROR, ex))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.CONFLICT,
                        ex.getTechnicalMessage()))
                .onErrorResume(TechnicalException.class, ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getTechnicalMessage()))
                .onErrorResume(ex -> {
                    log.error(Constants.UNEXPECTED_ERROR, ex);
                    return buildErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            TechnicalMessage.INTERNAL_ERROR);
                });
    }

    public Mono<ServerResponse> assignTechnologies(ServerRequest request) {
        return request.bodyToMono(AssignTechnologiesDTO.class)
                .flatMap(assignTechnologiesDTO ->
                    technologyServicePort.assignTechnologies(assignTechnologiesDTO.getCapacityID(), assignTechnologiesDTO.getTechnologiesIds())
                        .doOnSuccess(successful -> log.info(Constants.ASSIGN_TECHNOLOGIES_CREATED_RS_OK))
                )
                .then(ServerResponse
                    .status(HttpStatus.OK)
                    .bodyValue(new ResponseDTO(TechnicalMessage.TECHNOLOGIES_ASSIGN_OK.getMessage())))
                .doOnError(ex -> log.error(Constants.TECHNOLOGY_ERROR, ex))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.CONFLICT,
                        ex.getTechnicalMessage()))
                .onErrorResume(TechnicalException.class, ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getTechnicalMessage()))
                .onErrorResume(ex -> {
                    log.error(Constants.UNEXPECTED_ERROR, ex);
                    return buildErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            TechnicalMessage.INTERNAL_ERROR);
                });
    }

    public Mono<ServerResponse> getTechnologiesByCapabilities(ServerRequest request) {
        String capabilitiesParam = request.queryParam(Constants.QUERY_PARAM_CAPABILITIES_IDS).orElse("");
        List<Long> capabilitiesIds = Arrays.stream(capabilitiesParam.split(","))
            .map(Long::parseLong)
            .toList();
        return technologyServicePort.findTechnologiesByCapabilitiesIds(capabilitiesIds)
            .collectList()
            .doOnSuccess( capabilitiesList -> log.info(Constants.ASSIGN_TECHNOLOGIES_CREATED_RS_OK))
            .flatMap(list ->
                ServerResponse
                .status(HttpStatus.OK)
                .bodyValue(list))
            .doOnError(ex -> log.error(Constants.TECHNOLOGY_ERROR, ex))
            .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                    HttpStatus.CONFLICT,
                    ex.getTechnicalMessage()))
            .onErrorResume(TechnicalException.class, ex -> buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getTechnicalMessage()))
            .onErrorResume(ex -> {
                log.error(Constants.UNEXPECTED_ERROR, ex);
                return buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        TechnicalMessage.INTERNAL_ERROR);
            });
    }
    private Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus, TechnicalMessage error) {
        return Mono.defer(() -> {
            ErrorDTO errorResponse = ErrorDTO.builder()
                    .code(error.getCode())
                    .message(error.getMessage())
                    .build();
            return ServerResponse.status(httpStatus)
                    .bodyValue(errorResponse);
        });
    }
}
