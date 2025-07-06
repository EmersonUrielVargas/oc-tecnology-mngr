package com.onclass.technology.domain.api;

import com.onclass.technology.domain.model.Technology;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TechnologyServicePort {
    Mono<Technology> registerTechnology(Technology technology);
    Mono<Void> assignTechnologies(Long capabilityId, List<Long> technologiesIds);
}
