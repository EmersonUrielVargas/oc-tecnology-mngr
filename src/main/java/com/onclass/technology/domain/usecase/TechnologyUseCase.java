package com.onclass.technology.domain.usecase;

import com.onclass.technology.domain.api.TechnologyServicePort;
import com.onclass.technology.domain.enums.TechnicalMessage;
import com.onclass.technology.domain.exceptions.EntityAlreadyExistException;
import com.onclass.technology.domain.exceptions.EntityNotFoundException;
import com.onclass.technology.domain.exceptions.ParamRequiredMissingException;
import com.onclass.technology.domain.exceptions.TechnicalException;
import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.domain.model.spi.CapacityItem;
import com.onclass.technology.domain.spi.TechnologyPersistencePort;
import com.onclass.technology.domain.validators.Validator;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public Mono<Void> assignTechnologies(Long capabilityId, List<Long> technologiesIds) {
        if (capabilityId == null || technologiesIds.isEmpty()){
            return Mono.error(new ParamRequiredMissingException(TechnicalMessage.MISSING_REQUIRED_PARAM));
        }
        return Flux.fromIterable(technologiesIds)
            .distinct()
            .collectList()
            .flatMap( listIds -> technologyPersistencePort.findByIds(listIds).map(Technology::id).collectList())
            .flatMap( listTechnologies ->{
                if (listTechnologies.size() != technologiesIds.size()){
                    return Mono.error(new EntityNotFoundException(TechnicalMessage.SOME_TECHNOLOGIES_NOT_FOUND));
                }
                return technologyPersistencePort.assignTechnologies(capabilityId, listTechnologies);
            });
    }

    @Override
    public Flux<CapacityItem> findTechnologiesByCapabilitiesIds(List<Long> capabilitiesIds) {
        return technologyPersistencePort.findTechnologiesByCapabilitiesIds(capabilitiesIds);
    }
}
