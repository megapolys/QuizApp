package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.Role;
import com.example.servingwebcontent.repositories.UserRepository;
import com.example.servingwebcontent.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public User addUser(User user) {
        User userFromDb = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (userFromDb != null) {
            return null;
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
        return savedUser;
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
}
