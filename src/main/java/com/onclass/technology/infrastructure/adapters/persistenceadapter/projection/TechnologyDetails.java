package com.onclass.technology.infrastructure.adapters.persistenceadapter.projection;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TechnologyDetails {
	private Long id;
	private String name;
	private Long capacityId;
}
