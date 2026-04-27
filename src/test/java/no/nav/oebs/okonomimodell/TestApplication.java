package no.nav.oebs.okonomimodell;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Test-spesifikk applikasjonsklasse uten @EnableJwtTokenValidation.
 * JWT-validering deaktiveres i tester siden vi ikke har en ekte Azure AD tilgjengelig.
 */
@SpringBootApplication
public class TestApplication {
}

