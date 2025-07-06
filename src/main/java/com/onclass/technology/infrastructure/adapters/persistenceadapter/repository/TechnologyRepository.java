package com.onclass.technology.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.technology.infrastructure.adapters.persistenceadapter.entity.TechnologyEntity;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.projection.TechnologyDetails;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Repository
public interface TechnologyRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {
    Mono<TechnologyEntity> findByName(String name);

    @Query("""
        SELECT t.id,
            t.name,
            tc.id_capacity AS capacity_id
        FROM technology_capacity tc
        INNER JOIN technologies t ON tc.id_technology = t.id
        WHERE tc.id_capacity IN (:capabilitiesIds)
    """)
    Flux<TechnologyDetails> findCapabilitiesByBootcampsIds(List<Long> capabilitiesIds);
}
