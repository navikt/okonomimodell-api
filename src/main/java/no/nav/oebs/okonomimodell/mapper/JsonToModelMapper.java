package no.nav.oebs.okonomimodell.mapper;

import lombok.extern.slf4j.Slf4j;
import no.nav.oebs.okonomimodell.exception.InvalidJsonException;
import org.openapitools.model.Relasjon;
import org.openapitools.model.Segment;
import org.openapitools.model.SegmentType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Slf4j
@Component
public class JsonToModelMapper {

    private static final String RELASJON = "relasjon";
    private static final String SEGMENT_VERDI = "segmentVerdi";
    private static final String BESKRIVELSE = "beskrivelse";
    private static final String SEGMENT_TYPE = "segmentType";

    public List<Segment> mapJsonToSegments(List<String> segments) {
        return segments.stream()
                .map(this::mapJsonStringToSegment)
                .toList();
    }

    public Segment mapJsonStringToSegment(String jsonSegment) {
        try {
            JsonNode jsonNode = new JsonMapper().readTree(jsonSegment);
            JsonNode segmentNode = jsonNode.isArray() ? jsonNode.get(0) : jsonNode; // Assuming the JSON string can be either an array or a single object
            return mapJsonToSegment(segmentNode);
        } catch (Exception e) {
            throw new InvalidJsonException("Failed to parse JSON segment: " + jsonSegment);
        }
    }

    public Segment mapJsonToSegment(JsonNode jsonSegment) throws  InvalidJsonException {
        Segment segment = new Segment();
        segment.segmentVerdi(jsonSegment.get(SEGMENT_VERDI).asString());
        segment.setBeskrivelse(jsonSegment.get(BESKRIVELSE).asString());
        segment.setSegmentType(mapSegmentType(jsonSegment.get(SEGMENT_TYPE).asString()));

        List<Relasjon> relations = jsonSegment.get(RELASJON)
                .valueStream()
                .map(this::mapJsonToRelation)
                .toList();
        segment.setRelasjon(relations);

        return segment;
    }

    private SegmentType mapSegmentType(String segmentType) throws InvalidJsonException{
        try {
            return SegmentType.valueOf(segmentType);
        } catch (IllegalArgumentException e) {
            throw new InvalidJsonException("Unknown segment type: " + segmentType);
        }
    }

    private Relasjon mapJsonToRelation(JsonNode jsonRelation) throws InvalidJsonException{
        Relasjon relation = new Relasjon();
        relation.setSegmentType(mapSegmentType(jsonRelation.get(SEGMENT_TYPE).asString()));
        relation.setSegmentVerdi(jsonRelation.get(SEGMENT_VERDI).asString());
        return relation;
    }
}
