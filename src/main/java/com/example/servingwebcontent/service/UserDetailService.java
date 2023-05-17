package com.example.servingwebcontent.service;

import com.example.servingwebcontent.UserRepository;
import com.example.servingwebcontent.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
