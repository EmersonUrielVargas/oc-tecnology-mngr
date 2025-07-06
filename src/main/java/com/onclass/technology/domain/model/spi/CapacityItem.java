package com.onclass.technology.domain.model.spi;

import com.onclass.technology.domain.model.Technology;

import java.util.List;

public record CapacityItem (
	Long id,
	List<TechnologyItem> technologies
){}
