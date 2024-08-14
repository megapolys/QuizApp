package com.example.servingwebcontent.model.user;

import com.example.servingwebcontent.model.Role;
import com.example.servingwebcontent.model.medical.result.MedicalTopicResult;
import com.example.servingwebcontent.model.quiz.result.QuizResult;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Data
@Builder
public class User implements UserDetails {

	Long id;

	String username;

	String firstName;

	String lastName;

	String middleName;

	String email;

	LocalDate birthday;

	Boolean male;

	String activationCode;

	String repairPasswordCode;

	String password;

	/**
	 * Пароль, который дублирует первый для проверки
	 */
	String password2;

	Set<Role> roles;

	Set<QuizResult> results;

	Set<MedicalTopicResult> medicalResults;

	Boolean active;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}
}
