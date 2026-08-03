package no.nav.oebs.okonomimodell;

import lombok.AllArgsConstructor;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test-spesifikk applikasjonsklasse med MockOAuth2Server for å simulere JWT-validering i tester.
 */
@SpringBootTest
@AllArgsConstructor
class ApplicationTest {

    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }
}

