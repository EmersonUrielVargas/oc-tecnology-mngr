package com.onclass.technology.domain.usecase;

import com.onclass.technology.domain.api.TechnologyServicePort;
import com.onclass.technology.domain.enums.TechnicalMessage;
import com.onclass.technology.domain.exceptions.EntityAlreadyExistException;
import com.onclass.technology.domain.exceptions.TechnicalException;
import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.domain.spi.TechnologyPersistencePort;
import com.onclass.technology.domain.validators.Validator;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class TechnologyUseCase implements TechnologyServicePort {

    private final TechnologyPersistencePort technologyPersistencePort;

    @Override
    public Mono<Technology> registerTechnology(Technology technology) {
        return Validator.validateTechnology(technology)
            .flatMap(newTechnology->
                technologyPersistencePort.findByName(technology.name()))
            .flatMap( technologyFound -> Mono.error(new EntityAlreadyExistException(TechnicalMessage.TECHNOLOGY_ALREADY_EXISTS)))
            .switchIfEmpty(
                Mono.defer(()->
                        technologyPersistencePort.upsert(technology)
                        .switchIfEmpty(Mono.error(new TechnicalException(TechnicalMessage.ERROR_CREATING_TECHNOLOGY)))
                )
            ).cast(Technology.class);
    }
}
