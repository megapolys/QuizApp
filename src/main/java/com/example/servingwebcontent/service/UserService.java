package com.example.servingwebcontent.service;

import com.example.servingwebcontent.UserRepository;
import com.example.servingwebcontent.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userName = userRepository.findByUsername(username);
        return userName != null ? userName : userRepository.findByEmail(username);
    }
}
