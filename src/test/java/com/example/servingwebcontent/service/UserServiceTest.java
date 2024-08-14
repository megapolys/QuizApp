package com.example.servingwebcontent.service;

import com.example.servingwebcontent.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/application.yml")
class UserServiceTest {

	@Autowired
	private UserServiceImpl userService;

	@MockBean
	private UserRepository userRepository;
	@MockBean
	private JavaMailSender mailSender;
	@MockBean
	private PasswordEncoder passwordEncoder;
//
//    @Test
//    void addUser() {
//        final User user = new User();
//        user.setEmail("example@mail.ru");
//        final ArgumentCaptor<SimpleMailMessage> message = ArgumentCaptor.forClass(SimpleMailMessage.class);
//
//        final UserServiceImpl.UserResult userResult = userService.addUser(user);
//        assertEquals(UserServiceImpl.ResultType.SUCCESS, userResult.result());
//        assertNotNull(user.getActivationCode());
//        assertTrue(CoreMatchers.is(user.getRoles()).matches(Set.of(Role.USER)));
//        assertFalse(user.isActive());
//
//        Mockito.verify(userRepository, Mockito.times(1)).save(user);
//        Mockito.verify(mailSender, Mockito.times(1)).send(message.capture());
//        assertNotNull(message.getValue());
//        assertNotNull(message.getValue().getTo());
//        assertEquals(1, message.getValue().getTo().length);
//        assertEquals("example@mail.ru", message.getValue().getTo()[0]);
//        assertNotNull(message.getValue().getFrom());
//        assertNotNull(message.getValue().getText());
//        assertTrue(message.getValue().getText().contains("localhost:8080/activate/" + user.getActivationCode()), message.getValue().getText());
//    }
//
//    @Test
//    void addUserNameFailedTest() {
//        final User user = new User();
//        user.setUsername("Kiril");
//        final User toBeReturned = new User();
//        toBeReturned.setId(1L);
//        Mockito.doReturn(toBeReturned)
//            .when(userRepository)
//            .findByUsername("Kiril");
//        final UserServiceImpl.UserResult userResult = userService.addUser(user);
//        assertEquals(UserServiceImpl.ResultType.USERNAME_FOUND, userResult.result());
//
//        Mockito.verify(userRepository, Mockito.times(0)).save(any());
//        Mockito.verify(mailSender, Mockito.times(0)).send(any(SimpleMailMessage.class));
//    }
//
//    @Test
//    void addUserMailFailedTest() {
//        final User user = new User();
//        user.setEmail("example@mail.ru");
//        final User toBeReturned = new User();
//        toBeReturned.setId(1L);
//        Mockito.doReturn(toBeReturned)
//            .when(userRepository)
//            .findByEmail("example@mail.ru");
//        final UserServiceImpl.UserResult userResult = userService.addUser(user);
//        assertEquals(UserServiceImpl.ResultType.EMAIL_FOUND, userResult.result());
//
//        Mockito.verify(userRepository, Mockito.times(0)).save(any());
//        Mockito.verify(mailSender, Mockito.times(0)).send(any(SimpleMailMessage.class));
//    }
//
//    @Test
//    void activateUser() {
//        final String code = "activationCode";
//        final User user = new User();
//        user.setActivationCode(code);
//        user.setActive(false);
//        Mockito.doReturn(user)
//                .when(userRepository)
//                .findByActivationCode(code);
//        assertTrue(userService.activateUser(code));
//        assertNull(user.getActivationCode());
//        assertTrue(user.isActive());
//        Mockito.verify(userRepository, Mockito.times(1)).save(user);
//    }
//
//    @Test
//    void activateUserFailed() {
//        final String code = "activationCode";
//        final User user = new User();
//        user.setActivationCode(code);
//        user.setActive(false);
//        Mockito.doReturn(null)
//                .when(userRepository)
//                .findByActivationCode(code);
//        assertFalse(userService.activateUser(code));
//        assertNotNull(user.getActivationCode());
//        assertFalse(user.isActive());
//        Mockito.verify(userRepository, Mockito.times(0)).save(any());
//    }
//
//    @Test
//    void repairPassword() {
//        final User user = new User();
//        final String mail = "example@mail.ru";
//        user.setEmail(mail);
//        Mockito.doReturn(user)
//            .when(userRepository)
//            .findByEmail(mail);
//        final UserServiceImpl.UserResult userResult = userService.repairPassword(mail);
//        assertEquals(UserServiceImpl.ResultType.EMAIL_FOUND, userResult.result());
//        assertEquals(user, userResult.user());
//        assertNotNull(user.getRepairPasswordCode());
//        Mockito.verify(userRepository, Mockito.times(1)).save(user);
//    }
//
//    @Test
//    void repairPasswordFailed() {
//        final User user = new User();
//        final String mail = "example@mail.ru";
//        user.setEmail(mail);
//        Mockito.doReturn(null)
//            .when(userRepository)
//            .findByEmail(mail);
//        final UserServiceImpl.UserResult userResult = userService.repairPassword(mail);
//        assertEquals(UserServiceImpl.ResultType.FAILED, userResult.result());
//        assertNull(user.getRepairPasswordCode());
//        Mockito.verify(userRepository, Mockito.times(0)).save(any());
//    }
}