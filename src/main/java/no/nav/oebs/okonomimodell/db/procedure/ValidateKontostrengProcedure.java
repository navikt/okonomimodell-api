package no.nav.oebs.okonomimodell.db.procedure;

import lombok.extern.slf4j.Slf4j;
import no.nav.oebs.okonomimodell.model.Kontostreng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;

@Slf4j
@Repository
public class ValidateKontostrengProcedure {

    private static final String SCHEMA = "APPS";
    private static final String PACKAGE = "XXRTV_OKONOMIMODELL_API_PKG";
    private static final String PROCEDURE = "xxrtv_validerkontostreng_api";

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

    private static final String OUT_PARAM_VALID = "p_valid";
    private static final String OUT_PARAM_MESSAG = "p_message";

    private final SimpleJdbcCall validateKontostrengCall;

    @Autowired
    public ValidateKontostrengProcedure(DataSource dataSource) {
        this.validateKontostrengCall = new SimpleJdbcCall(dataSource)
                .withSchemaName(SCHEMA)
                .withCatalogName(PACKAGE)
                .withProcedureName(PROCEDURE)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
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
                        new SqlOutParameter(OUT_PARAM_VALID, Types.VARCHAR),
                        new SqlOutParameter(OUT_PARAM_MESSAG, Types.VARCHAR)
                );
    }

    public boolean executeValidateKontostrengProcedure(Kontostreng kontostreng) {
        Map<String, Object> result = getValidateKontostreng(kontostreng);
        String valid = (String) result.get(OUT_PARAM_VALID);
        String message = (String) result.get(OUT_PARAM_MESSAG);
        log.info("Executing procedure with result valid={}, message={}", valid, message);
        return "Y".equals(valid);
    }

    public Map<String, Object> getValidateKontostreng(Kontostreng kontostreng) {
        MapSqlParameterSource inputParams = new MapSqlParameterSource()
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
                .addValue(IN_PARAM_SYSTEM, "LONN"); //todo: should system be included?

        return validateKontostrengCall.execute(inputParams);
    }
}
