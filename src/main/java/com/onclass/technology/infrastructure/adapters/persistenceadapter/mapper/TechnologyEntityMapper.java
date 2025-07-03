package com.onclass.technology.infrastructure.adapters.persistenceadapter.mapper;

import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.entity.TechnologyEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface TechnologyEntityMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Technology toModel(TechnologyEntity entity);

    @InheritInverseConfiguration
    TechnologyEntity toEntity(Technology technology);
}
