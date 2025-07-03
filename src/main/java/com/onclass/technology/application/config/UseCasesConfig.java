package com.onclass.technology.application.config;

import com.onclass.technology.domain.api.TechnologyServicePort;
import com.onclass.technology.domain.spi.TechnologyPersistencePort;
import com.onclass.technology.domain.usecase.TechnologyUseCase;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.TechnologyPersistenceAdapter;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.mapper.TechnologyEntityMapper;
import com.onclass.technology.infrastructure.adapters.persistenceadapter.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {
        private final TechnologyRepository technologyRepository;
        private final TechnologyEntityMapper technologyEntityMapper;

        @Bean
        public TechnologyPersistencePort technologyPersistencePort() {
                return new TechnologyPersistenceAdapter(technologyRepository,technologyEntityMapper);
        }

        @Bean
        public TechnologyServicePort technologyServicePort(TechnologyPersistencePort technologyPersistencePort){
                return new TechnologyUseCase(technologyPersistencePort);
        }
}
