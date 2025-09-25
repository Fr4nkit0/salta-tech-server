package com.saltaTech.user.application.service.implementations;

import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
import com.saltaTech.auth.domain.persistence.OrganizationMember;
import com.saltaTech.auth.domain.repository.OrganizationMemberRepository;
import com.saltaTech.auth.domain.repository.RoleRepository;
import com.saltaTech.common.application.aop.OrganizationSecured;
import com.saltaTech.organization.application.exceptions.OrganizationNotFoundException;
import com.saltaTech.organization.application.mapper.OrganizationMapper;
import com.saltaTech.organization.domain.repository.OrganizationRepository;
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
@OrganizationSecured
@Transactional
public class UserServiceImpl implements UserService {
	private final UserMapper userMapper;
	private final OrganizationMapper organizationMapper;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final RoleRepository roleRepository;
    private final OrganizationMemberRepository organizationMemberRepository;


	public UserServiceImpl(UserRepository userRepository, OrganizationRepository organizationRepository, RoleRepository roleRepository, OrganizationMemberRepository organizationMemberRepository, UserMapper userMapper, OrganizationMapper organizationMapper) {
		this.userRepository = userRepository;
		this.organizationRepository = organizationRepository;
		this.roleRepository = roleRepository;
		this.organizationMemberRepository = organizationMemberRepository;
		this.userMapper = userMapper;
		this.organizationMapper = organizationMapper;
	}

	@Transactional
    @Override
    public RegisteredUser createUser(UserCreateRequest request) {
		var user = userMapper.toUser(request);
		userRepository.save(user);
		final var tenant = OrganizationContext.getOrganizationTenant();
		final var organization = organizationRepository.findActiveByTenant(tenant)
				.orElseThrow(() -> new OrganizationNotFoundException(tenant));
		final var role = roleRepository.findById(request.roleId())
				.orElseThrow(() -> new RuntimeException("Rol no encontrado"));
		OrganizationMember member = organizationMapper.toOrganizationMember(organization, user, role);
		return userMapper.toRegisteredUser(organizationMemberRepository.save(member));
	}

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        return null;
    }
}
