package com.example.servingwebcontent.controller.auth;

import com.example.servingwebcontent.Application;
import com.example.servingwebcontent.repositories.RoleRepository;
import com.example.servingwebcontent.repositories.UserRepository;
import com.example.servingwebcontent.repositories.UserRoleRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public abstract class AuthControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected UserRepository userRepository;

	@MockBean
	protected RoleRepository roleRepository;

	@MockBean
	protected UserRoleRepository userRoleRepository;

	@MockBean
	protected RestTemplate restTemplate;

	@MockBean
	protected JavaMailSender mailSender;

	@MockBean
	protected PasswordEncoder passwordEncoder;
}
