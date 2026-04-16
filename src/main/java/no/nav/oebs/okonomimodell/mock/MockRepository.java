package no.nav.oebs.okonomimodell.mock;

import lombok.RequiredArgsConstructor;
import no.nav.oebs.okonomimodell.mapper.JsonToModelMapper;
import org.openapitools.model.Segment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MockRepository {

    private final JsonToModelMapper jsonToModelMapper;
    private final JsonMapper jsonMapper;

    public List<Segment> findAll() {
        try {
            var resource = new ClassPathResource("db-example-respons/all-segments.json");
            JsonNode root = jsonMapper.readTree(resource.getInputStream());
            return mapJsonToSegments(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Segment> mapJsonToSegments(JsonNode jsonSegments) {
        List<Segment> segments = new java.util.ArrayList<>(List.of());
        jsonSegments.valueStream().map(jsonToModelMapper::mapJsonToSegment).forEach(segments::add);
        return segments;
    }
}
