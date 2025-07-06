package com.onclass.technology.infrastructure.adapters.persistenceadapter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "technology_capacity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyCapacityEntity {
    @Id
    private Long id;
    @Column("id_technology")
    private Long technologyId;

    @Column("id_capacity")
    private Long capacityId;
}
