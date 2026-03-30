package no.nav.oebs.kontoplan_api.repository;

import lombok.RequiredArgsConstructor;
import no.nav.oebs.kontoplan_api.model.KonteringsInfoResponse;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class KonteringsInfoRepository {

    private final JdbcClient jdbcClient;

    // TODO: Replace view name and column names once OeBS schema is confirmed
    public List<KonteringsInfoResponse> findActive(String orgId, String segmentnavn, String system) {
        // TODO: implement query once view name and schema are confirmed
        throw new UnsupportedOperationException(
                "OeBS view name not yet confirmed — implement query before enabling");
    }
}
