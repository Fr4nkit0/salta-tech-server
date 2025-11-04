package com.saltaTech.common.application.aop;

import com.saltaTech.auth.application.security.authentication.context.BranchContext;
import com.saltaTech.common.domain.persistence.Filters;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class BranchSecuredAspect {
	private final EntityManager entityManager;

	public BranchSecuredAspect(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Around("@annotation(organizationSecured) || @within(organizationSecured)")
	public Object applyOrganizationFilter(ProceedingJoinPoint joinPoint, BranchSecured organizationSecured) throws Throwable {
		if (organizationSecured == null) {
			organizationSecured = joinPoint.getTarget().getClass().getAnnotation(BranchSecured.class);
		}

		Session session = entityManager.unwrap(Session.class);
		String tenant= BranchContext.getBranchTenant();

		if (tenant != null) {
			log.debug("Enabling organization filter for tenant: {}", tenant);
			session.enableFilter(Filters.BRANCH_FILTER)
					.setParameter(Filters.BRANCH_SLUG_PARAM, tenant);
		}

		try {
			return joinPoint.proceed();
		} finally {
			if (tenant != null) {
				session.disableFilter(Filters.BRANCH_FILTER);
				log.debug("Organization filter disabled");
			}
		}
	}
}

