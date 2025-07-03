package com.onclass.technology.infrastructure.adapters.persistenceadapter;

import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.domain.spi.TechnologyPersistencePort;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.mapper.TechnologyEntityMapper;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.repository.TechnologyRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class TechnologyPersistenceAdapter implements TechnologyPersistencePort {
    private final TechnologyRepository technologyRepository;
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
}
