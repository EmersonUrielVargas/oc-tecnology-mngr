package com.onclass.technology.infrastructure.entrypoints.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class DeleteTechnologiesByCapabilitiesDTO {
    private List<Long> capabilitiesId;
}
