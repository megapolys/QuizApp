package com.example.servingwebcontent.service.user.impl;

import com.example.servingwebcontent.exceptions.UserNotFoundException;
import com.example.servingwebcontent.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserPersistence userPersistence;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		UserDetails userByName = userPersistence.findUserDetailsByUsername(login);
		if (userByName != null) {
			return userByName;
		}
		UserDetails userByEmail = userPersistence.findUserDetailsByEmail(login);
		if (userByEmail != null) {
			return userByEmail;
		}
		throw UserNotFoundException.byLogin(login);
	}

}
