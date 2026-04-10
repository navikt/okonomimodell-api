package no.nav.oebs.okonomimodell.mapper;

import org.openapitools.model.Segment;
import org.openapitools.model.SegmentType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.List;

@Component
public class JsonToModelMapper {

    public List<Segment> mapJsonToSegments(JsonNode jsonSegments) {
        List<Segment> segments = new java.util.ArrayList<>(List.of());
        for (JsonNode elem : jsonSegments.asArray()) {
            var segment = mapJsonToSegment(elem);
            segments.add(segment);
        }
        return segments;
    }

    public Segment mapJsonToSegment(JsonNode jsonSegment) {
        Segment segment = new Segment();
        segment.setSegmentKey(jsonSegment.get("Key").toString());
        segment.setDescription(jsonSegment.get("Description").toString());
        segment.setSegmentType(SegmentType.ARTSKONTO);
        return segment;
    }
}
