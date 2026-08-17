package no.nav.oebs.okonomimodell.repository.procedure;

import no.nav.oebs.okonomimodell.dto.Kontostreng;
import no.nav.oebs.okonomimodell.repository.ValidateKontostrengProcedure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.util.ReflectionTestUtils;

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

    private ValidateKontostrengProcedure procedure;

    @BeforeEach
    void setUp() {
        procedure = new ValidateKontostrengProcedure(dataSource);
        ReflectionTestUtils.setField(procedure, "validateKontostrengCall", simpleJdbcCall);
    }

    @Test
    void executeValidateKontostrengProcedure_shouldReturnTrueWhenValidIsY() {
        when(simpleJdbcCall.execute(any(SqlParameterSource.class)))
                .thenReturn(Map.of("p_valid", "Y", "p_message", "OK"));

        assertTrue(procedure.executeValidateKontostrengProcedure(kontostreng()));
    }

    @Test
    void executeValidateKontostrengProcedure_shouldReturnFalseWhenValidIsN() {
        when(simpleJdbcCall.execute(any(SqlParameterSource.class)))
                .thenReturn(Map.of("p_valid", "N", "p_message", "Ugyldig kontostreng"));

        assertFalse(procedure.executeValidateKontostrengProcedure(kontostreng()));
    }

    @Test
    void executeValidateKontostrengProcedure_shouldReturnFalseWhenValidIsNull() {
        Map<String, Object> result = new HashMap<>();
        result.put("p_valid", null);
        result.put("p_message", null);
        when(simpleJdbcCall.execute(any(SqlParameterSource.class))).thenReturn(result);

        assertFalse(procedure.executeValidateKontostrengProcedure(kontostreng()));
    }

    @Test
    void executeValidateKontostrengProcedure_shouldBeCaseSensitive_lowercaseYReturnsFalse() {
        when(simpleJdbcCall.execute(any(SqlParameterSource.class)))
                .thenReturn(Map.of("p_valid", "y", "p_message", ""));

        assertFalse(procedure.executeValidateKontostrengProcedure(kontostreng()));
    }

    @Test
    void executeValidateKontostrengProcedure_shouldCallSimpleJdbcCall() {
        when(simpleJdbcCall.execute(any(SqlParameterSource.class)))
                .thenReturn(Map.of("p_valid", "Y", "p_message", "OK"));

        procedure.executeValidateKontostrengProcedure(kontostreng());

        verify(simpleJdbcCall).execute(any(SqlParameterSource.class));
    }

    @Test
    void getValidateKontostreng_shouldReturnRawResultFromProcedure() {
        var expected = Map.<String, Object>of("p_valid", "Y", "p_message", "OK");
        when(simpleJdbcCall.execute(any(SqlParameterSource.class))).thenReturn(expected);

        Map<String, Object> result = procedure.getValidateKontostreng(kontostreng());

        assertEquals(expected, result);
    }

    private static Kontostreng kontostreng() {
        return Kontostreng.of("281000000000", "857410", null, null, null, null, null, null, null, null, null, null);
    }
}
