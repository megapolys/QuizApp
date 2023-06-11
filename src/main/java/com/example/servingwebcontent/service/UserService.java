package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.Role;
import com.example.servingwebcontent.repositories.UserRepository;
import com.example.servingwebcontent.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Value("${spring.mail.from}")
    private String from;

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public UserService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByName = userRepository.findByUsername(username);
        if (userByName != null) {
            return userByName;
        }
        User userByEmail = userRepository.findByEmail(username);
        if (userByEmail != null) {
            return userByEmail;
        }
        throw new UsernameNotFoundException("Пользователь не найден");
    }

    @NonNull
    public UserResult addUser(User user) {
        UserResult result = checkUser(user);
        if (result != null) {
            return result;
        }
        user.setActive(false);
        user.setRoles(Set.of(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        final User savedUser = userRepository.save(user);
        if (StringUtils.hasText(user.getEmail())) {
            final String message = String.format(
                    "Здравствуйте, %s \n" +
                            "Добро пожаловать на Health-3800. Пожалуйста перейдите по ссылки для подтверждения регистрации: http://localhost:8080/activate/%s",
                    user.getUsername(), user.getActivationCode()
            );
            final SimpleMailMessage simpleMessage = new SimpleMailMessage();
            simpleMessage.setFrom(from);
            simpleMessage.setTo(user.getEmail());
            simpleMessage.setSubject("Код активации");
            simpleMessage.setText(message);
            mailSender.send(simpleMessage);
        }
        return new UserResult(ResultType.SUCCESS, savedUser);
    }

    @NonNull
    public UserResult updateUser(User currentUser, User newUser) {
        currentUser.setUsername(newUser.getUsername());
        currentUser.setPassword(newUser.getPassword());
        currentUser.setEmail(newUser.getEmail());
        UserResult result = checkUser(currentUser);
        if (result != null) {
            return result;
        }
        return new UserResult(ResultType.SUCCESS, userRepository.save(currentUser));
    }

    private UserResult checkUser(User user) {
        User byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername != null && !Objects.equals(byUsername.getId(), user.getId())) {
            return new UserResult(ResultType.USERNAME_FOUND, null);
        }
        User byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail != null && !Objects.equals(byEmail.getId(), user.getId())) {
            return new UserResult(ResultType.EMAIL_FOUND, null);
        }
        return null;
    }

    public boolean activateUser(String code) {
        final User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);

        userRepository.save(user);

        return true;
    }

    public enum ResultType {
        USERNAME_FOUND, EMAIL_FOUND, SUCCESS
    }

    public record UserResult(ResultType result, User user) {
    }
}
