package com.saltaTech.user.application.mapper;

import com.saltaTech.auth.domain.persistence.OrganizationMember;
import com.saltaTech.user.domain.dto.request.UserCreateRequest;
import com.saltaTech.user.domain.dto.response.RegisteredUser;
import com.saltaTech.user.domain.persistence.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

	/*
    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.isSuperUser()
        );
    } */

	public User toUser (UserCreateRequest request){
		if (request == null) return null;
		return User.builder()
				.firstName(request.firstName())
				.lastName(request.lastName())
				.email(request.email())
				.phoneNumber(request.phoneNumber())
				.isSuperUser(false)
				.build();
	}
	public RegisteredUser toRegisteredUser(OrganizationMember member){
		if (member == null) return null;
		return new RegisteredUser(
				new RegisteredUser.UserInfo(
						member.getUser().getId(),
						member.getUser().getFirstName(),
						member.getUser().getLastName(),
						member.getUser().getEmail(),
						member.getUser().getPhoneNumber()
				),
				member.getOrganization().getSlug(),
				member.getRole().getName(),
				member.getBranchAccesses().stream()
				.map(ba -> new RegisteredUser.BranchAccessInfo(
						ba.getBranch().getName())
				).toList()
		);
	}

}
