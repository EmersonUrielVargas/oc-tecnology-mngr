package com.onclass.technology.infrastructure.adapters.persistenceadapter;

import com.onclass.technology.domain.enums.OrderList;
import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.domain.model.spi.CapacityItem;
import com.onclass.technology.domain.model.spi.TechnologyItem;
import com.onclass.technology.domain.spi.TechnologyPersistencePort;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.entity.TechnologyCapacityEntity;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.mapper.TechnologyEntityMapper;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.projection.TechnologiesCount;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.projection.TechnologyDetails;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.repository.TechnologyCapacityRepository;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.repository.TechnologyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class TechnologyPersistenceAdapter implements TechnologyPersistencePort {
    private final TechnologyRepository technologyRepository;
    private final TechnologyCapacityRepository technologyCapacityRepository;
    private final TechnologyEntityMapper technologyEntityMapper;

    @Override
    public Mono<Technology> upsert(Technology technology) {
        return technologyRepository.save(technologyEntityMapper.toEntity(technology))
                .map(technologyEntityMapper::toModel);
    }

    @Override
    public Mono<Technology> findByName(String nameTechnology) {
        return technologyRepository.findByName(nameTechnology)
                .map(technologyEntityMapper::toModel);
    }

    @Override
    public Mono<Void> assignTechnologies(Long capabilityId, List<Long> technologyIds) {
        return Flux.fromIterable(technologyIds)
            .flatMap(technologyId ->
                technologyCapacityRepository.save(
                    TechnologyCapacityEntity.builder()
                        .capacityId(capabilityId)
                        .technologyId(technologyId).build()
                )
            ).then();
    }

    @Override
    public Flux<Technology> findByIds(List<Long> technologyIds) {
        return technologyRepository.findAllById(technologyIds).map(technologyEntityMapper::toModel);
    }

    @Override
    public Mono<Technology> findById(Long technologyId) {
        return technologyRepository.findById(technologyId).map(technologyEntityMapper::toModel);
    }

    @Override
    public Flux<CapacityItem> findTechnologiesByCapabilitiesIds(List<Long> capabilitiesIds) {
        return technologyRepository.findCapabilitiesByBootcampsIds(capabilitiesIds)
            .groupBy(TechnologyDetails::getCapacityId)
            .flatMap(techGroup ->
                techGroup.map( technologyDetails ->
                    new TechnologyItem(technologyDetails.getId(), technologyDetails.getName())
                ).collectList()
                .map( technologies -> new CapacityItem(techGroup.key(), technologies))
            );
    }

    @Override
    public Flux<CapacityItem> findPaginatedCapabilitiesByTechnologiesNumber(OrderList order, int page, int size) {
        return sortTechnologiesCountPaginated(order, page, size)
            .doOnNext(list -> log.info("lista que llega {}", list))
            .map(TechnologiesCount::getIdCapacity)
            .collectList()
            .flatMapMany(this::findTechnologiesByCapabilitiesIds);
    }

    @Override
    public Mono<Long> countTotalCapacities() {
        return technologyCapacityRepository.countCapabilities();
    }

    private Flux<TechnologiesCount> sortTechnologiesCountPaginated(OrderList order, int page, int size) {
        int offset = page*size;
        return switch (order){
            case ASCENDANT -> technologyCapacityRepository.findCapabilitiesIdsOrderByTechnologiesCountAsc(size, offset);
            case DESCENDANT -> technologyCapacityRepository.findCapabilitiesIdsOrderByTechnologiesCountDesc(size, offset);
        };
    }


}
