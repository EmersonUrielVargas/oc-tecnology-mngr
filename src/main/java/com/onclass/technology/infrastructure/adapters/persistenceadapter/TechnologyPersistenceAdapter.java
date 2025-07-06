package com.onclass.technology.infrastructure.adapters.persistenceadapter;

import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.domain.spi.TechnologyPersistencePort;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.entity.TechnologyCapacityEntity;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.mapper.TechnologyEntityMapper;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.repository.TechnologyCapacityRepository;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.repository.TechnologyRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
}
