package com.onclass.technology.infrastructure.entrypoints;

import com.onclass.technology.infrastructure.entrypoints.dto.request.AssignTechnologiesDTO;
import com.onclass.technology.infrastructure.entrypoints.dto.request.CreateTechnologyDTO;
import com.onclass.technology.infrastructure.entrypoints.handler.TechnologyHandlerImpl;
import com.onclass.technology.infrastructure.entrypoints.util.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.onclass.technology.infrastructure.entrypoints.util.Constants.*;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
     @RouterOperations({
        @RouterOperation(
                path = PATH_POST_TECHNOLOGY,
                produces = {"application/json"},
                method = POST,
                beanClass = TechnologyHandlerImpl.class,
                beanMethod = "createTechnology",
                operation = @Operation(
                        operationId = "createTechnology",
                        summary = "Create a technology",
                        security = @SecurityRequirement(name = "BearerAuth"),
                        requestBody = @RequestBody(
                                required = true,
                                content = @Content(schema = @Schema(implementation = CreateTechnologyDTO.class))
                        ),
                        responses = {
                                @ApiResponse(
                                        responseCode = "201",
                                        description = TECHNOLOGY_CREATED_RS_OK,
                                        content = @Content(schema = @Schema(implementation = ResponseDTO.class))
                                ),
                                @ApiResponse(
                                        responseCode = "409",
                                        description = "Error validation information"
                                ),
                                @ApiResponse(responseCode = "401", description = "Unauthorized")
                        }
                )
        ),
         @RouterOperation(
            path = PATH_POST_ASSIGN_TECHNOLOGIES,
            produces = {"application/json"},
            method = POST,
            beanClass = TechnologyHandlerImpl.class,
            beanMethod = "assignTechnologies",
            operation = @Operation(
                    operationId = "assignTechnologies",
                    summary = "Assign a list of technologies to a capacity",
                    security = @SecurityRequirement(name = "BearerAuth"),
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(schema = @Schema(implementation = AssignTechnologiesDTO.class))
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = ASSIGN_TECHNOLOGIES_CREATED_RS_OK,
                                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))
                            ),
                            @ApiResponse(
                                    responseCode = "409",
                                    description = "Error validation information"
                            ),
                            @ApiResponse(responseCode = "401", description = "Unauthorized")
                    }
            )
        )
     })
    public RouterFunction<ServerResponse> routerFunction(TechnologyHandlerImpl technologyHandler) {
        return route(POST(PATH_POST_TECHNOLOGY), technologyHandler::createTechnology)
            .andRoute(POST(PATH_POST_ASSIGN_TECHNOLOGIES), technologyHandler::assignTechnologies)
            .andRoute(GET(PATH_GET_TECHNOLOGIES_BY_CAPABILITIES_IDS),technologyHandler::getTechnologiesByCapabilities);
    }
}
