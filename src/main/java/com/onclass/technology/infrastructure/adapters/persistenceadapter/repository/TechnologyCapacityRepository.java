package com.onclass.technology.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.technology.infrastructure.adapters.persistenceadapter.entity.TechnologyCapacityEntity;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.projection.TechnologiesCount;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Repository
public interface TechnologyCapacityRepository extends ReactiveCrudRepository<TechnologyCapacityEntity, Long> {

	@Query("""
        SELECT tc.id_capacity, COUNT(tc.id_technology) AS technologies_count
        FROM technology_capacity tc
        GROUP BY tc.id_capacity
        ORDER BY technologies_count DESC
        LIMIT :size OFFSET :offset
    """)
	Flux<TechnologiesCount> findCapabilitiesIdsOrderByTechnologiesCountDesc(int size, int offset);

    @Query("""
        SELECT tc.id_capacity, COUNT(tc.id_technology) AS technologies_count
        FROM technology_capacity tc
        GROUP BY tc.id_capacity
        ORDER BY technologies_count DESC
        LIMIT :size OFFSET :offset
    """)
    Flux<TechnologiesCount> findCapabilitiesIdsOrderByTechnologiesCountAsc(int size, int offset);

	@Query("""
        SELECT COUNT(DISTINCT tc.id_capacity)
        FROM technology_capacity tc;
    """)
	Mono<Long> countCapabilities();

	@Modifying
	@Query("DELETE FROM technology_capacity WHERE id_capacity IN (:capacityIds)")
	Mono<Void> deleteAllByCapabilitiesIds(List<Long> capabilitiesIds);

	@Query("""
        SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
        FROM technology_capacity
        WHERE id_technology = :technologyId AND id_capacity NOT IN (:capabilitiesIds);
    """)
	Mono<Boolean> verifyOtherAssignations(Long technologyId, List<Long> capabilitiesIds);
}
