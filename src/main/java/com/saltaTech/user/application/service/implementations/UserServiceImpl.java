package com.saltaTech.user.application.service.implementations;

import com.saltaTech.auth.application.security.authentication.context.BranchContext;
import com.saltaTech.auth.domain.persistence.BranchMember;
import com.saltaTech.auth.domain.repository.BranchMemberRepository;
import com.saltaTech.auth.domain.repository.RoleRepository;
import com.saltaTech.common.application.aop.BranchSecured;
import com.saltaTech.branch.application.exceptions.BranchNotFoundException;
import com.saltaTech.branch.application.mapper.BranchMapper;
import com.saltaTech.branch.domain.repository.BranchRepository;
import com.saltaTech.user.application.mapper.UserMapper;
import com.saltaTech.user.application.service.interfaces.UserService;
import com.saltaTech.user.domain.dto.request.UserCreateRequest;
import com.saltaTech.user.domain.dto.response.RegisteredUser;
import com.saltaTech.user.domain.dto.response.UserResponse;
import com.saltaTech.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@BranchSecured
@Transactional
public class UserServiceImpl implements UserService {
	private final UserMapper userMapper;
	private final BranchMapper branchMapper;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final RoleRepository roleRepository;
    private final BranchMemberRepository organizationMemberRepository;


	public UserServiceImpl(UserRepository userRepository, BranchRepository branchRepository, RoleRepository roleRepository, BranchMemberRepository organizationMemberRepository, UserMapper userMapper, BranchMapper branchMapper) {
		this.userRepository = userRepository;
		this.branchRepository = branchRepository;
		this.roleRepository = roleRepository;
		this.organizationMemberRepository = organizationMemberRepository;
		this.userMapper = userMapper;
		this.branchMapper = branchMapper;
	}

	@Transactional
    @Override
    public RegisteredUser createUser(UserCreateRequest request) {
		var user = userMapper.toUser(request);
		userRepository.save(user);
		final var tenant = BranchContext.getBranchTenant();
		final var branch = branchRepository.findActiveByTenant(tenant)
				.orElseThrow(() -> new BranchNotFoundException(tenant));
		final var role = roleRepository.findById(request.roleId())
				.orElseThrow(() -> new RuntimeException("Rol no encontrado"));
		BranchMember member = branchMapper.toBranchMember(branch, user, role);
		return userMapper.toRegisteredUser(organizationMemberRepository.save(member));
	}

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        return null;
    }
}
