package com.onclass.technology.infrastructure.adapters.persistenceadapter.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "technologies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyEntity {
    @Id
    private Long id;
    private String name;
    private String description;
}
