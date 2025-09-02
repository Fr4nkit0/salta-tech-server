package com.saltaTech.auth.application.security.authentication.adapters;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SuperUserDetails implements UserDetails {
	private final String email;
	private final String password;

	public SuperUserDetails(String email, String password) {
		this.email = email;
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_SUPERUSER"));
	}

	@Override
	public String getPassword() {
		return this.password ;

	}
	@Override
	public String getUsername() {
		return this.email;
	}
}
