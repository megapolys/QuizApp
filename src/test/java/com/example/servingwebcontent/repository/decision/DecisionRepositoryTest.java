package com.example.servingwebcontent.repository.decision;

import com.example.servingwebcontent.Application;
import com.example.servingwebcontent.repositories.DecisionRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public abstract class DecisionRepositoryTest {

	@Autowired
	protected DecisionRepository decisionRepository;

}
