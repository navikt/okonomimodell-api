package no.nav.oebs.okonomimodell.repository;

import lombok.RequiredArgsConstructor;
import no.nav.oebs.okonomimodell.model.Segment;
import no.nav.oebs.okonomimodell.model.SegmentResponse;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OkonomimodellRepository {

    private final JdbcClient jdbcClient;

    // TODO: Replace view name and column names once OeBS schema is confirmed
    private static final String BASE_QUERY = """
            SELECT segment, segment_key, description, default_segment, default_segment_key,
                   valid_from, valid_to
            FROM TODO_OEBS_VIEW_NAME
            """;

    public List<SegmentResponse> findAll() {
        // TODO: implement query once view name and schema are confirmed
        throw new UnsupportedOperationException(
                "OeBS view name not yet confirmed — update BASE_QUERY before enabling");
    }

    public List<SegmentResponse> findBySegmenttype(Segment segmenttype) {
        // TODO: implement query once view name and schema are confirmed
        throw new UnsupportedOperationException(
                "OeBS view name not yet confirmed — update BASE_QUERY before enabling");
    }
}
