package com.saltaTech.common.application.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
		name = "Bearer Authentication",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer"
)
@SecurityScheme(
		name = "Branch",
		type = SecuritySchemeType.APIKEY,
		in = SecuritySchemeIn.HEADER,
		paramName = "X-Branch-Tenant"
)
public class OpenApiConfiguration {
		@Bean
		public OpenAPI customOpenAPI() {
			return new OpenAPI()
					.info(new Info()
							.title("Salta Tech")
							.version("1.0")
							.description("Salta Tech es una plataforma multi-tenant que permite a las organizaciones gestionar e integrar sus datos de manera segura y escalable."+
									" La API expone un conjunto de controladores diseñados para interactuar con nuestros servicios, facilitando la autenticación, el manejo de recursos"+
									" y la operación en entornos con múltiples sucursales de forma aislada y eficiente."+
									" Cada petición debe incluir el identificador de la sucursal, lo que garantiza el aislamiento de datos entre diferentes clientes."))
					.addSecurityItem(new SecurityRequirement()
							.addList("Bearer Authentication"))
					.addSecurityItem(new SecurityRequirement()
							.addList("Branch"));
		}
}
