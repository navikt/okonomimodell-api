package no.nav.oebs.okonomimodell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;

@SpringBootApplication
@EnableJwtTokenValidation
public class OkonomimodellApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkonomimodellApplication.class, args);
	}

}
