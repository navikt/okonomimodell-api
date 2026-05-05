package no.nav.oebs.okonomimodell;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;

@SpringBootApplication
@EnableJwtTokenValidation(ignore = {"org.springdoc", "org.springframework"})
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", name = "BearerAuth")
public class OkonomimodellApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkonomimodellApplication.class, args);
	}

}
