package com.onclass.technology.domain.usecase;

import com.onclass.technology.domain.enums.TechnicalMessage;
import com.onclass.technology.domain.exceptions.BusinessException;
import com.onclass.technology.domain.exceptions.EntityAlreadyExistException;
import com.onclass.technology.domain.exceptions.EntityNotFoundException;
import com.onclass.technology.domain.exceptions.ParamRequiredMissingException;
import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.domain.spi.TechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TechnologyUseCaseTest {

    @Mock
    private TechnologyPersistencePort persistencePort;

    @InjectMocks
    private TechnologyUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new TechnologyUseCase(persistencePort);
    }

    @Test
    void registerTechnology_shouldSaveUniqueTechnologies() {
        Technology technology = new Technology(null, "Java", "Backend");
        Technology technologyBD = new Technology(1L, "Java", "Backend");

        when(persistencePort.findByName("Java")).thenReturn(Mono.empty());

        when(persistencePort.upsert(technology)).thenReturn(Mono.just(technologyBD));

        StepVerifier.create(useCase.registerTechnology(technology))
                .expectNext(technologyBD)
                .verifyComplete();

        verify(persistencePort).findByName("Java");
        verify(persistencePort).upsert(technology);
    }

    @Test
    void registerTechnology_shouldErrorTechnologies() {
        Technology technology = new Technology(null, "Java", "Backend");
        Technology technologyBD = new Technology(1L, "Java", "Backend");

        when(persistencePort.findByName("Java")).thenReturn(Mono.just(technologyBD));

        StepVerifier.create(useCase.registerTechnology(technology))
                .expectErrorMatches(error -> error instanceof EntityAlreadyExistException &&
                        ((BusinessException) error).getTechnicalMessage() == TechnicalMessage.TECHNOLOGY_ALREADY_EXISTS)
                .verify();
    }

    @Test
    void assignTechnologies_shouldValidateAndSave() {
        Long capabilityId = 1L;
        List<Long> techIds = List.of(1L, 2L);
        List<Technology> technologyList = List.of(
            new Technology(1L, null, null),
            new Technology(2L, null, null)
        );

        when(persistencePort.findByIds(techIds)).thenReturn(Flux.fromIterable(technologyList));
        when(persistencePort.assignTechnologies(capabilityId, techIds)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.assignTechnologies(capabilityId, techIds))
                .verifyComplete();

        verify(persistencePort).assignTechnologies(capabilityId, techIds);
    }

    @Test
    void assignTechnologies_shouldThrowErrorTechNotFound() {
        Long capabilityId = 1L;
        List<Long> techIds = List.of(1L, 2L);
        List<Technology> technologyList = List.of(
            new Technology(1L, null, null)
        );

        when(persistencePort.findByIds(techIds)).thenReturn(Flux.fromIterable(technologyList));

        StepVerifier.create(useCase.assignTechnologies(capabilityId, techIds))
                .expectErrorMatches(error -> error instanceof EntityNotFoundException &&
                        ((EntityNotFoundException) error).getTechnicalMessage() == TechnicalMessage.SOME_TECHNOLOGIES_NOT_FOUND)
                .verify();

    }

    @Test
    void assignTechnologies_shouldThrowErrorInvalidParams() {
        Long capabilityId = 1L;
        List<Long> techIds = List.of();
        StepVerifier.create(useCase.assignTechnologies(capabilityId, techIds))
                .expectErrorMatches(error -> error instanceof ParamRequiredMissingException &&
                        ((ParamRequiredMissingException) error).getTechnicalMessage() == TechnicalMessage.MISSING_REQUIRED_PARAM)
                .verify();

    }
}