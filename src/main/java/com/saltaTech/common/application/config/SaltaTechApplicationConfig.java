package com.saltaTech.common.application.config;

import com.saltaTech.user.domain.persistence.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class SaltaTechApplicationConfig {
	@Bean
	public AuditorAware<User> auditorAware() {
		return new ApplicationAuditorAware();
	}
}
