package com.onclass.technology.infrastructure.entrypoints.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CreateTechnologyDTO {
    private String name;
    private String description;
}
