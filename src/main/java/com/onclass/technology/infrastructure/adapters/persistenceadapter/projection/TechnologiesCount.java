package com.onclass.technology.infrastructure.adapters.persistenceadapter.projection;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TechnologiesCount {
	private Long idCapacity;
	private Long technologiesCount;
}
