package com.onclass.tecnology;

import com.onclass.technology.TecnologyApplication;
import com.onclass.technology.domain.spi.TechnologyPersistencePort;
import com.onclass.technology.domain.usecase.TechnologyUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = TecnologyApplication.class)
class TechnologyApplicationTests {

	@MockitoBean
	private TechnologyPersistencePort technologyPersistencePort;

	@Autowired
	private TechnologyUseCase technologyUseCase;

	@Test
	void contextLoads() {
	}

}
