package no.nav.oebs.okonomimodell;

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Test-spesifikk applikasjonsklasse med MockOAuth2Server for å simulere JWT-validering i tester.
 */
@SpringBootApplication
@EnableJwtTokenValidation
@EnableMockOAuth2Server
public class TestApplication {
}

