package no.nav.oebs.okonomimodell.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KontostrengTest {

    @Test
    void of_shouldUseProvidedValuesWhenNotNull() {
        var k = Kontostreng.of("artskonto", "ksted", "produkt", "oppgave", "felles", "statskonto", "kilde", "2024", "ff1", "ff2", "fullmakt", "regnskapsf");

        assertEquals("artskonto", k.artskonto());
        assertEquals("ksted", k.kostnadssted());
        assertEquals("produkt", k.produkt());
        assertEquals("oppgave", k.oppgave());
        assertEquals("felles", k.felles());
        assertEquals("statskonto", k.statskonto());
        assertEquals("kilde", k.kilde());
        assertEquals("2024", k.tilsagnsaar());
        assertEquals("ff1", k.frittfelt1());
        assertEquals("ff2", k.frittfelt2());
        assertEquals("fullmakt", k.fullmaktskode());
        assertEquals("regnskapsf", k.regnskapsforer());
    }

    @Test
    void of_shouldUseDefaultValuesForNullFields() {
        var k = Kontostreng.of(null, null, null, null, null, null, null, null, null, null, null, null);

        assertEquals("000000000000", k.artskonto());
        assertEquals("000000", k.kostnadssted());
        assertEquals("000000", k.produkt());
        assertEquals("000000", k.oppgave());
        assertEquals("000000000000", k.felles());
        assertEquals("00", k.statskonto());
        assertEquals("000000", k.kilde());
        assertEquals("000000", k.tilsagnsaar());
        assertEquals("00", k.frittfelt1());
        assertEquals("000000", k.frittfelt2());
        assertEquals("000000", k.fullmaktskode());
        assertEquals("000000", k.regnskapsforer());
    }

    @Test
    void of_shouldUseDefaultOnlyForNullFields() {
        var k = Kontostreng.of("281000000000", null, null, null, null, null, null, null, null, null, null, null);

        assertEquals("281000000000", k.artskonto());
        assertEquals("000000", k.kostnadssted());
    }
}
