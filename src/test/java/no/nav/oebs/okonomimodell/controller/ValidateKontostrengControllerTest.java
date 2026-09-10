package no.nav.oebs.okonomimodell.controller;

import no.nav.oebs.okonomimodell.service.OkonomimodellService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.KontostrengValidation;
import org.openapitools.model.System;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateKontostrengControllerTest {

    @Mock
    private OkonomimodellService okonomimodellService;

    private ValidateKontostrengController controller;

    @BeforeEach
    void setUp() {
        controller = new ValidateKontostrengController(okonomimodellService);
    }

    @Test
    void validateKontostreng_shouldReturnTrueWhenServiceReturnsTrue() {
        when(okonomimodellService.getKontostrengValidation(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(new KontostrengValidation().valid(true));

        var result = controller.validateKontostreng(System.LONN, "281000000000", "857410", null, null, null, null, null, null, null, null, null, null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(Boolean.TRUE, result.getBody().getValid());
    }

    @Test
    void validateKontostreng_shouldReturnFalseWhenServiceReturnsFalse() {
        when(okonomimodellService.getKontostrengValidation(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(new KontostrengValidation().valid(false));

        var result = controller.validateKontostreng(System.LONN, "ugyldig", null, null, null, null, null, null, null, null, null, null, null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(Boolean.FALSE, result.getBody().getValid());
    }

    @Test
    void validateKontostreng_shouldPassAllParametersToService() {
        when(okonomimodellService.getKontostrengValidation(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(new KontostrengValidation().valid(true));

        controller.validateKontostreng(System.LONN, "artskonto", "kostnadssted", "produkt", "oppgave", "felles", "statskonto", "kilde", "2024", "ff1", "ff2", "fullmakt", "regnskapsf");

        verify(okonomimodellService).getKontostrengValidation(
                System.LONN,
                "artskonto", "kostnadssted", "produkt", "oppgave", "felles",
                "statskonto", "kilde", "2024", "ff1", "ff2", "fullmakt", "regnskapsf");
    }

    @Test
    void validateKontostreng_shouldHandleAllNullParameters() {
        when(okonomimodellService.getKontostrengValidation(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(new KontostrengValidation().valid(false));

        var result = controller.validateKontostreng(System.LONN, null, null, null, null, null, null, null, null, null, null, null, null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }
}
