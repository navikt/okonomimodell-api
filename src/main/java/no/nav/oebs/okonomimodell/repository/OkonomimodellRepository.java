package no.nav.oebs.okonomimodell.repository;

import lombok.RequiredArgsConstructor;
import no.nav.oebs.okonomimodell.mapper.JsonToModelMapper;
import org.openapitools.model.Segment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OkonomimodellRepository {

    private final JsonToModelMapper jsonToModelMapper;
    private final ObjectMapper objectMapper;

    //private final JdbcClient jdbcClient;

    // TODO: Replace view name and column names once OeBS schema is confirmed
    private static final String BASE_QUERY = """
            SELECT segment, segment_key, description, default_segment, default_segment_key,
                   valid_from, valid_to
            FROM TODO_OEBS_VIEW_NAME
            """;

    public List<Segment> findAll() {
        // TODO: implement query once view name and schema are confirmed
        try {
            var resource = new ClassPathResource("db-example-respons/all-segments.json");
            JsonNode root = objectMapper.readTree(resource.getInputStream());
            return jsonToModelMapper.mapJsonToSegments(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        throw new UnsupportedOperationException(
//                "OeBS view name not yet confirmed — update BASE_QUERY before enabling");
    }

    public List<Segment> findBySegmenttype(Segment segmenttype) {
        // TODO: implement query once view name and schema are confirmed
        throw new UnsupportedOperationException(
                "OeBS view name not yet confirmed — update BASE_QUERY before enabling");
    }
}
