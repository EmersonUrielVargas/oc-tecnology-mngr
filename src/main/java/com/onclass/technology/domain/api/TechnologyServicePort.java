package com.onclass.technology.domain.api;

import com.onclass.technology.domain.enums.OrderList;
import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.domain.model.spi.CapacityItem;
import com.onclass.technology.domain.utilities.CustomPage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyServicePort {
    Mono<Technology> registerTechnology(Technology technology);
    Mono<Void> assignTechnologies(Long capabilityId, List<Long> technologiesIds);
    Flux<CapacityItem> findTechnologiesByCapabilitiesIds(List<Long> capabilitiesIds);
    Mono<CustomPage<CapacityItem>> findPaginatedCapabilitiesByTechnologies(OrderList order, int page, int size);
}
