package no.nav.oebs.okonomimodell.repository.procedure;

import no.nav.oebs.okonomimodell.dto.Kontostreng;
import no.nav.oebs.okonomimodell.dto.ValidateRespons;
import no.nav.oebs.okonomimodell.exception.InvalidOebsResponseException;
import no.nav.oebs.okonomimodell.repository.ValidateKontostrengProcedure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.KontostrengValidation;
import org.openapitools.model.System;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.util.ReflectionTestUtils;
import tools.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateKontostrengProcedureTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private SimpleJdbcCall simpleJdbcCall;

    @Mock
    private ObjectMapper objectMapper;

    private ValidateKontostrengProcedure procedure;

    @BeforeEach
    void setUp() {
        procedure = new ValidateKontostrengProcedure(dataSource, objectMapper);
        ReflectionTestUtils.setField(procedure, "validateKontostrengCall", simpleJdbcCall);
    }

    @Test
    void executeValidateKontostrengProcedure_shouldReturnTrueWhenCcidIsPresent() throws Exception {
        String response = "{\"Ccid\":\"12345\",\"ValidateMessage\":\"OK\"}";
        when(simpleJdbcCall.execute(any(SqlParameterSource.class)))
                .thenReturn(Map.of("p_json_message", response));
        when(objectMapper.readValue(response, ValidateRespons.class))
                .thenReturn(new ValidateRespons("12345", "OK"));

        KontostrengValidation result = procedure.executeValidateKontostrengProcedure(System.LONN, kontostreng());
        assertEquals(Boolean.TRUE, result.getValid());
        assertNull(result.getFeilmeldingOebs());
    }

    @Test
    void executeValidateKontostrengProcedure_shouldReturnFalseAndMessageWhenCcidIsEmpty() throws Exception {
        String response = "{\"Ccid\":\"\",\"ValidateMessage\":\"Ugyldig kontostreng\"}";
        when(simpleJdbcCall.execute(any(SqlParameterSource.class)))
                .thenReturn(Map.of("p_json_message", response));
        when(objectMapper.readValue(response, ValidateRespons.class))
                .thenReturn(new ValidateRespons("", "Ugyldig kontostreng"));

        KontostrengValidation result = procedure.executeValidateKontostrengProcedure(System.LONN, kontostreng());
        assertEquals(Boolean.FALSE, result.getValid());
        assertEquals("Ugyldig kontostreng", result.getFeilmeldingOebs());
    }

    @Test
    void executeValidateKontostrengProcedure_shouldThrowWhenCcidIsNull() throws Exception {
        String response = "{\"Ccid\":null,\"ValidateMessage\":\"Ugyldig kontostreng\"}";
        Map<String, Object> result = new HashMap<>();
        result.put("p_json_message", response);
        when(simpleJdbcCall.execute(any(SqlParameterSource.class))).thenReturn(result);
        when(objectMapper.readValue(response, ValidateRespons.class))
                .thenReturn(new ValidateRespons(null, "Ugyldig kontostreng"));

        assertThrows(InvalidOebsResponseException.class,
                () -> procedure.executeValidateKontostrengProcedure(System.LONN, kontostreng()));
    }

    @Test
    void executeValidateKontostrengProcedure_shouldThrowWhenResponseIsEmpty() {
        when(simpleJdbcCall.execute(any(SqlParameterSource.class)))
                .thenReturn(Map.of("p_json_message", ""));

        assertThrows(InvalidOebsResponseException.class,
                () -> procedure.executeValidateKontostrengProcedure(System.LONN, kontostreng()));
    }

    @Test
    void executeValidateKontostrengProcedure_shouldCallSimpleJdbcCall() throws Exception {
        String response = "{\"Ccid\":\"12345\",\"ValidateMessage\":\"OK\"}";
        when(simpleJdbcCall.execute(any(SqlParameterSource.class)))
                .thenReturn(Map.of("p_json_message", response));
        when(objectMapper.readValue(response, ValidateRespons.class))
                .thenReturn(new ValidateRespons("12345", "OK"));

        procedure.executeValidateKontostrengProcedure(System.LONN, kontostreng());

        verify(simpleJdbcCall).execute(any(SqlParameterSource.class));
    }

    @Test
    void getValidateKontostreng_shouldReturnRawResultFromProcedure() {
        var expected = Map.<String, Object>of("p_json_message", "{\"Ccid\":\"12345\",\"ValidateMessage\":\"OK\"}");
        when(simpleJdbcCall.execute(any(SqlParameterSource.class))).thenReturn(expected);

        Map<String, Object> result = procedure.getValidateKontostreng(System.LONN, kontostreng());

        assertEquals(expected, result);
    }

    private static Kontostreng kontostreng() {
        return Kontostreng.of("281000000000", "857410", null, null, null, null, null, null, null, null, null, null);
    }
}
