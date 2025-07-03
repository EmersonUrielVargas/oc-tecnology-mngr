package com.onclass.technology.domain.api;

import com.onclass.technology.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface TechnologyServicePort {
    Mono<Technology> registerTechnology(Technology technology);
}
