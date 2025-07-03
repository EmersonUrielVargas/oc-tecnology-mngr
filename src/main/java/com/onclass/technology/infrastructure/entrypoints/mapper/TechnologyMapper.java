package com.onclass.technology.infrastructure.entrypoints.mapper;

import com.onclass.technology.domain.model.Technology;
import com.onclass.technology.infrastructure.entrypoints.dto.request.CreateTechnologyDTO;
import com.onclass.technology.infrastructure.entrypoints.dto.response.TechnologyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface TechnologyMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    TechnologyDTO toTechnologyDto(Technology technology);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Technology toTechnology(CreateTechnologyDTO createTechnologyDTO);
}
