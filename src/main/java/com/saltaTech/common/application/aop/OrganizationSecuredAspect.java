package com.saltaTech.common.application.aop;

import com.saltaTech.auth.application.security.authentication.context.OrganizationContext;
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
public class OrganizationSecuredAspect {
	private final EntityManager entityManager;

	public OrganizationSecuredAspect(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Around("@annotation(organizationSecured) || @within(organizationSecured)")
	public Object applyOrganizationFilter(ProceedingJoinPoint joinPoint, OrganizationSecured organizationSecured) throws Throwable {
		if (organizationSecured == null) {
			organizationSecured = joinPoint.getTarget().getClass().getAnnotation(OrganizationSecured.class);
		}

		Session session = entityManager.unwrap(Session.class);
		String tenant= OrganizationContext.getOrganizationTenant();

		if (tenant != null) {
			log.debug("Enabling organization filter for tenant: {}", tenant);
			session.enableFilter(Filters.ORGANIZATION_FILTER)
					.setParameter(Filters.ORGANIZATION_SLUG_PARAM, tenant);
		}

		try {
			return joinPoint.proceed();
		} finally {
			if (tenant != null) {
				session.disableFilter(Filters.ORGANIZATION_FILTER);
				log.debug("Organization filter disabled");
			}
		}
	}
}

