package com.onclass.technology.infrastructure.entrypoints.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class BasicTechnologyDTO {
    private Long id;
    private String name;
}
