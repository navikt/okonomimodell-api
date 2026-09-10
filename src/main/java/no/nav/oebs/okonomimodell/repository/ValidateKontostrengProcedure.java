package no.nav.oebs.okonomimodell.repository;

import no.nav.oebs.okonomimodell.exception.InvalidOebsResponseException;
import org.openapitools.model.KontostrengValidation;
import tools.jackson.core.JacksonException;
import lombok.extern.slf4j.Slf4j;
import no.nav.oebs.okonomimodell.dto.Kontostreng;
import no.nav.oebs.okonomimodell.dto.ValidateRespons;
import org.openapitools.model.System;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class ValidateKontostrengProcedure {

    private static final String SCHEMA = "apps";
    private static final String PACKAGE = "xxrtv_gl_val_kontostreng_pkg";
    private static final String PROCEDURE = "validerkstreng";

    private static final String IN_PARAM_ORGID = "p_org_id";
    private static final String IN_PARAM_ARTSKONTO = "p_artskonto";
    private static final String IN_PARAM_KOSTNADSSTED= "p_ksted";
    private static final String IN_PARAM_PRODUKT = "p_produktoppgave";
    private static final String IN_PARAM_OPPGAVE = "p_deloppgave";
    private static final String IN_PARAM_FELLES = "p_fellesoppgave";
    private static final String IN_PARAM_STATSKONTO = "p_statskonto";
    private static final String IN_PARAM_TILSGNINGSAR = "p_tilsagnsaar";
    private static final String IN_PARAM_KILDE = "p_kilde";
    private static final String IN_PARAM_FRTTFELT1 = "p_fritt_felt_1";
    private static final String IN_PARAM_FRTTFELT2 = "p_fritt_felt_2";
    private static final String IN_PARAM_FULLMAKTSKODE = "p_fullmaktskode";
    private static final String IN_PARAM_REGNSKAPSFORER = "p_regnskapsforer";
    private static final String IN_PARAM_SYSTEM = "p_system";

    private static final String OUT_PARAM_MESSAGE = "p_json_message";

    private final SimpleJdbcCall validateKontostrengCall;
    private final ObjectMapper objectMapper;

    @Autowired
    public ValidateKontostrengProcedure(DataSource dataSource, ObjectMapper objectMapper) {
        this.validateKontostrengCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(SCHEMA)
                .withCatalogName(PACKAGE)
                .withProcedureName(PROCEDURE)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter(IN_PARAM_ORGID, Types.NUMERIC),
                        new SqlParameter(IN_PARAM_ARTSKONTO, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_KOSTNADSSTED, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_PRODUKT, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_OPPGAVE, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_FELLES, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_STATSKONTO, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_KILDE, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_TILSGNINGSAR, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_FRTTFELT1, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_FRTTFELT2, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_FULLMAKTSKODE, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_REGNSKAPSFORER, Types.VARCHAR),
                        new SqlParameter(IN_PARAM_SYSTEM, Types.VARCHAR),
                        new SqlOutParameter(OUT_PARAM_MESSAGE, Types.VARCHAR)
                );
        this.objectMapper = objectMapper;
    }

    public KontostrengValidation executeValidateKontostrengProcedure(System system, Kontostreng kontostreng) {
        Map<String, Object> result = getValidateKontostreng(system, kontostreng);
        String outMessage = (String) result.get(OUT_PARAM_MESSAGE);

        try {
            ValidateRespons validateRespons = parseValidateRespons(outMessage);

            // If ccid has a value then the kontostreng is valid, otherwise it is invalid
            String ccid = validateRespons.ccid();
            if (ccid == null) {
                throw new InvalidOebsResponseException("CCID is null in the response from the procedure.");
            }
            boolean valid = !ccid.isEmpty();
            KontostrengValidation validation = new KontostrengValidation().valid(valid);
            if (!valid) {
                validation.setFeilmeldingOebs(validateRespons.validateMessage());
            }
            return validation;
        } catch (InvalidOebsResponseException e) {
            log.error("Validation failed for kontostreng {} from system {} with outMessage {}: {}",
                    kontostreng, system, outMessage, e.getMessage());
            throw e;
        }

    }

    public Map<String, Object> getValidateKontostreng(System system, Kontostreng kontostreng) {
        MapSqlParameterSource inputParams = new MapSqlParameterSource()
                .addValue(IN_PARAM_ORGID, 202)
                .addValue(IN_PARAM_ARTSKONTO, kontostreng.artskonto())
                .addValue(IN_PARAM_KOSTNADSSTED, kontostreng.kostnadssted())
                .addValue(IN_PARAM_PRODUKT, kontostreng.produkt())
                .addValue(IN_PARAM_OPPGAVE, kontostreng.oppgave())
                .addValue(IN_PARAM_FELLES, kontostreng.felles())
                .addValue(IN_PARAM_STATSKONTO, kontostreng.statskonto())
                .addValue(IN_PARAM_KILDE, kontostreng.kilde())
                .addValue(IN_PARAM_TILSGNINGSAR, kontostreng.tilsagnsaar())
                .addValue(IN_PARAM_FRTTFELT1, kontostreng.frittfelt1())
                .addValue(IN_PARAM_FRTTFELT2, kontostreng.frittfelt2())
                .addValue(IN_PARAM_FULLMAKTSKODE, kontostreng.fullmaktskode())
                .addValue(IN_PARAM_REGNSKAPSFORER, kontostreng.regnskapsforer())
                .addValue(IN_PARAM_SYSTEM, system);

        return validateKontostrengCall.execute(inputParams);
    }

    private ValidateRespons parseValidateRespons(String outMessage) {
        if (outMessage == null || outMessage.isEmpty()) {
            throw new InvalidOebsResponseException("OeBS did return an empty response to validation procedure processing kontostreng.");
        }
        try {
            String trimmed = outMessage.trim(); //hva gjør trim?
            if (trimmed.startsWith("[")){
                List<ValidateRespons> responsList = objectMapper.readValue(
                        trimmed, new TypeReference<List<ValidateRespons>>() {});
                if (responsList.isEmpty()) {
                    throw new InvalidOebsResponseException("OeBS response list: " + outMessage + " is empty for kontostreng validation.");
                }
                return responsList.getFirst();
            }
            return objectMapper.readValue(trimmed, ValidateRespons.class);
        } catch (JacksonException e) {
            throw new InvalidOebsResponseException(
                    "Parsing of OeBS response to validationResponse failed. Response: " + outMessage, e);
        }
    }
}
