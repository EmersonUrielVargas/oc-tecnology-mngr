package com.onclass.technology.domain.spi;

import com.onclass.technology.domain.enums.OrderList;
import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.domain.model.spi.CapacityItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyPersistencePort {
    Mono<Technology> upsert(Technology technology);
    Mono<Technology> findByName(String nameTechnology);
    Mono<Void> assignTechnologies(Long capabilityId, List<Long> technologyIds);
    Flux<Technology> findByIds(List<Long> technologyIds);
    Mono<Technology> findById(Long technologyId);
    Flux<CapacityItem> findTechnologiesByCapabilitiesIds(List<Long> capabilitiesIds);
    Flux<CapacityItem> findPaginatedCapabilitiesByTechnologiesNumber(OrderList order, int page, int size);
    Mono<Long> countTotalCapacities();
    Mono<Void> deleteAllTechnologies(List<Long> technologiesIds);
    Mono<Void> deleteAllAssignations(List<Long> capabilitiesIds);
    Mono<Boolean> verifyOtherAssignations(Long technologyId, List<Long> capabilitiesIds);
}
