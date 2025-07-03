package com.onclass.technology.infrastructure.entrypoints.handler;

import com.onclass.technology.domain.api.TechnologyServicePort;
import com.onclass.technology.domain.enums.TechnicalMessage;
import com.onclass.technology.domain.exceptions.BusinessException;
import com.onclass.technology.domain.exceptions.TechnicalException;
import com.onclass.technology.infrastructure.entrypoints.dto.request.CreateTechnologyDTO;
import com.onclass.technology.infrastructure.entrypoints.dto.response.TechnologyDTO;
import com.onclass.technology.infrastructure.entrypoints.mapper.TechnologyMapper;
import com.onclass.technology.infrastructure.entrypoints.util.Constants;
import com.onclass.technology.infrastructure.entrypoints.util.ErrorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
                        .bodyValue(TechnicalMessage.TECHNOLOGY_CREATED.getMessage()))
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
