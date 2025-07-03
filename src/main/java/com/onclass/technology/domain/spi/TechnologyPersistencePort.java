package com.onclass.technology.domain.spi;

import com.onclass.technology.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface TechnologyPersistencePort {
    Mono<Technology> upsert(Technology technology);
    Mono<Technology> findByName(String nameTechnology);
}
