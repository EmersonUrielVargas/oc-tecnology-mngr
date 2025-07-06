package com.onclass.technology.infrastructure.entrypoints.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class AssignTechnologiesDTO {
    private Long capacityID;
    private List<Long> technologiesIds;
}
