package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.Role;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.repositories.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("/application-test.yml")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JavaMailSender mailSender;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void addUser() {
        final User user = new User();
        user.setEmail("example@mail.ru");
        final ArgumentCaptor<SimpleMailMessage> message = ArgumentCaptor.forClass(SimpleMailMessage.class);

        final UserService.UserResult userResult = userService.addUser(user);
        assertEquals(UserService.ResultType.SUCCESS, userResult.result());
        assertNotNull(user.getActivationCode());
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Set.of(Role.USER)));
        assertFalse(user.isActive());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1)).send(message.capture());
        assertNotNull(message.getValue());
        assertNotNull(message.getValue().getTo());
        assertEquals(1, message.getValue().getTo().length);
        assertEquals("example@mail.ru", message.getValue().getTo()[0]);
        assertNotNull(message.getValue().getFrom());
        assertNotNull(message.getValue().getText());
        assertTrue(message.getValue().getText().contains("localhost:8080/activate/" + user.getActivationCode()), message.getValue().getText());
    }

    @Test
    void addUserNameFailedTest() {
        final User user = new User();
        user.setUsername("Kiril");
        final User toBeReturned = new User();
        toBeReturned.setId(1L);
        Mockito.doReturn(toBeReturned)
                .when(userRepository)
                .findByUsername("Kiril");
        final UserService.UserResult userResult = userService.addUser(user);
        assertEquals(UserService.ResultType.USERNAME_FOUND, userResult.result());

        Mockito.verify(userRepository, Mockito.times(0)).save(any());
        Mockito.verify(mailSender, Mockito.times(0)).send(any(SimpleMailMessage.class));
    }

    @Test
    void addUserMailFailedTest() {
        final User user = new User();
        user.setEmail("example@mail.ru");
        final User toBeReturned = new User();
        toBeReturned.setId(1L);
        Mockito.doReturn(toBeReturned)
                .when(userRepository)
                .findByEmail("example@mail.ru");
        final UserService.UserResult userResult = userService.addUser(user);
        assertEquals(UserService.ResultType.EMAIL_FOUND, userResult.result());

        Mockito.verify(userRepository, Mockito.times(0)).save(any());
        Mockito.verify(mailSender, Mockito.times(0)).send(any(SimpleMailMessage.class));
    }

    @Test
    void activateUser() {
        final String code = "activationCode";
        final User user = new User();
        user.setActivationCode(code);
        user.setActive(false);
        Mockito.doReturn(user)
                .when(userRepository)
                .findByActivationCode(code);
        assertTrue(userService.activateUser(code));
        assertNull(user.getActivationCode());
        assertTrue(user.isActive());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void activateUserFailed() {
        final String code = "activationCode";
        final User user = new User();
        user.setActivationCode(code);
        user.setActive(false);
        Mockito.doReturn(null)
                .when(userRepository)
                .findByActivationCode(code);
        assertFalse(userService.activateUser(code));
        assertNotNull(user.getActivationCode());
        assertFalse(user.isActive());
        Mockito.verify(userRepository, Mockito.times(0)).save(any());
    }

    @Test
    void repairPassword() {
        final User user = new User();
        final String mail = "example@mail.ru";
        user.setEmail(mail);
        Mockito.doReturn(user)
                .when(userRepository)
                .findByEmail(mail);
        final UserService.UserResult userResult = userService.repairPassword(mail);
        assertEquals(UserService.ResultType.EMAIL_FOUND, userResult.result());
        assertEquals(user, userResult.user());
        assertNotNull(user.getRepairPasswordCode());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void repairPasswordFailed() {
        final User user = new User();
        final String mail = "example@mail.ru";
        user.setEmail(mail);
        Mockito.doReturn(null)
                .when(userRepository)
                .findByEmail(mail);
        final UserService.UserResult userResult = userService.repairPassword(mail);
        assertEquals(UserService.ResultType.FAILED, userResult.result());
        assertNull(user.getRepairPasswordCode());
        Mockito.verify(userRepository, Mockito.times(0)).save(any());
    }
}