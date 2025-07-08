package com.onclass.technology.domain.usecase;

import com.onclass.technology.domain.api.TechnologyServicePort;
import com.onclass.technology.domain.enums.OrderList;
import com.onclass.technology.domain.enums.TechnicalMessage;
import com.onclass.technology.domain.exceptions.*;
import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.domain.model.spi.CapacityItem;
import com.onclass.technology.domain.model.spi.TechnologyItem;
import com.onclass.technology.domain.spi.TechnologyPersistencePort;
import com.onclass.technology.domain.utilities.CustomPage;
import com.onclass.technology.domain.validators.Validator;
import lombok.AllArgsConstructor;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class TechnologyUseCase implements TechnologyServicePort {

    private final TechnologyPersistencePort technologyPersistencePort;

    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Technology> registerTechnology(Technology technology) {
        return transactionalOperator.transactional(
            Validator.validateTechnology(technology)
            .flatMap(newTechnology->
                technologyPersistencePort.findByName(technology.name()))
            .flatMap( technologyFound -> Mono.error(new EntityAlreadyExistException(TechnicalMessage.TECHNOLOGY_ALREADY_EXISTS)))
            .switchIfEmpty(
                Mono.defer(()->
                        technologyPersistencePort.upsert(technology)
                        .switchIfEmpty(Mono.error(new TechnicalException(TechnicalMessage.ERROR_CREATING_TECHNOLOGY)))
                )
            ).cast(Technology.class)
        );
    }

    @Override
    public Mono<Void> assignTechnologies(Long capabilityId, List<Long> technologiesIds) {
        if (capabilityId == null || technologiesIds.isEmpty()){
            return Mono.error(new ParamRequiredMissingException(TechnicalMessage.MISSING_REQUIRED_PARAM));
        }
        return transactionalOperator.transactional(
            Flux.fromIterable(technologiesIds)
            .distinct()
            .collectList()
            .flatMap( listIds -> technologyPersistencePort.findByIds(listIds).map(Technology::id).collectList())
            .flatMap( listTechnologies ->{
                if (listTechnologies.size() != technologiesIds.size()){
                    return Mono.error(new EntityNotFoundException(TechnicalMessage.SOME_TECHNOLOGIES_NOT_FOUND));
                }
                return technologyPersistencePort.assignTechnologies(capabilityId, listTechnologies);
            })
        );
    }

    @Override
    public Flux<CapacityItem> findTechnologiesByCapabilitiesIds(List<Long> capabilitiesIds) {
        return technologyPersistencePort.findTechnologiesByCapabilitiesIds(capabilitiesIds);
    }

    @Override
    public Mono<CustomPage<CapacityItem>> findPaginatedCapabilitiesByTechnologies(OrderList order, int page, int size) {
        return technologyPersistencePort.findPaginatedCapabilitiesByTechnologiesNumber(order, page, size)
            .collectList()
            .zipWith(technologyPersistencePort.countTotalCapacities())
            .map(tuple ->
                CustomPage.buildCustomPage(tuple.getT1(), page, size, tuple.getT2())
            );
    }

    @Override
    public Mono<Void> deleteTechnologiesByCapabilitiesIds(List<Long> capabilitiesIds) {
        return transactionalOperator.transactional(
            Validator.validationCondition(!capabilitiesIds.isEmpty(), new InvalidFormatParamException(TechnicalMessage.INVALID_PARAMETERS))
            .then(
                 technologyPersistencePort.findTechnologiesByCapabilitiesIds(capabilitiesIds)
                .collectList()
                .flatMap( capabilitiesList -> {
                    Mono<List<Long>> techsToDelete = Flux.fromIterable(capabilitiesList)
                        .flatMap(capacity ->
                            Flux.fromIterable(capacity.technologies())
                        )
                        .map(TechnologyItem::id)
                        .distinct()
                        .flatMap(techId ->
                            technologyPersistencePort.verifyOtherAssignations(techId, capabilitiesIds)
                            .filter(haveOtherAssign -> !haveOtherAssign)
                            .map(valid -> techId)
                        ).collectList();

                    return techsToDelete
                        .flatMap(techIds ->{
                            if (techIds.isEmpty()){
                                return Mono.empty();
                            }
                            return technologyPersistencePort.deleteAllTechnologies(techIds);
                        })
                        .thenReturn(capabilitiesList);
                })
                .flatMapMany(Flux::fromIterable)
                .map(CapacityItem::id)
                .distinct()
                .collectList()
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        return Mono.empty();
                    }
                    return technologyPersistencePort.deleteAllAssignations(list);
                })
            )
        );
    }
}
