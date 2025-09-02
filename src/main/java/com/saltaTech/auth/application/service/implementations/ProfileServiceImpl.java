package com.saltaTech.auth.application.service.implementations;

import com.saltaTech.auth.application.mapper.ProfileMapper;
import com.saltaTech.auth.application.service.interfaces.ProfileService;
import com.saltaTech.auth.domain.dto.response.CurrentUserResponse;
import com.saltaTech.auth.domain.dto.response.ProfileResponse;
import org.springframework.stereotype.Service;


@Service
public class ProfileServiceImpl implements ProfileService {
	private final ProfileMapper profileMapper ;

	public ProfileServiceImpl(ProfileMapper profileMapper) {
		this.profileMapper = profileMapper;
	}

	@Override
	public CurrentUserResponse getCurrentUser() {
		return profileMapper.toCurrentUserResponse();
	}

	@Override
	public ProfileResponse getCurrentProfile() {
		return profileMapper.toProfileResponse();
	}

} 