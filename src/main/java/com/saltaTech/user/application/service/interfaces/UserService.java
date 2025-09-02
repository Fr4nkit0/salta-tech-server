package com.saltaTech.user.application.service.interfaces;

import com.saltaTech.user.domain.dto.request.UserCreateRequest;
import com.saltaTech.user.domain.dto.response.RegisteredUser;
import com.saltaTech.user.domain.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

	Page<UserResponse> findAll (Pageable pageable);

	RegisteredUser createUser(UserCreateRequest request);
}
