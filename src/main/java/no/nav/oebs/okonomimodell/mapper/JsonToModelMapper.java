package no.nav.oebs.okonomimodell.mapper;

import org.openapitools.model.Relasjon;
import org.openapitools.model.Segment;
import org.openapitools.model.SegmentType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.List;

@Component
public class JsonToModelMapper {

    public List<Segment> mapJsonToSegments(JsonNode jsonSegments) {
        List<Segment> segments = new java.util.ArrayList<>(List.of());
        jsonSegments.valueStream().map(this::mapJsonToSegment).forEach(segments::add);
        return segments;
    }

    private Segment mapJsonToSegment(JsonNode jsonSegment) {
        Segment segment = new Segment();
        segment.segmentVerdi(jsonSegment.get("Key").asString());
        segment.setBeskrivelse(jsonSegment.get("Description").asString());
        segment.setSegmentType(mapSegmentType(jsonSegment.get("SegmentName").asString()));

        String defaultDimention = "DefaultDimension";
        if (jsonSegment.has(defaultDimention) && !jsonSegment.get(defaultDimention).isNull()) {
            jsonSegment.get(defaultDimention)
                    .valueStream()
                    .forEach(s -> segment.addRelasjonItem(mapJsonToRelation(s)));
        }
        return segment;
    }

    private SegmentType mapSegmentType(String segmentType) {
        return switch (segmentType) {
            case "OR_KSTED" -> SegmentType.KOSTNADSSTED;
            case "OR_ART" -> SegmentType.ARTSKONTO;
            case "OR_PRODUKT" -> SegmentType.PRODUKT;
            case "OR_OPPGAVE" -> SegmentType.OPPGAVE;
            case "OR_STATSKONTO" -> SegmentType.STATSREGNSKAPSKONTO;
            case "OR_FELLES" -> SegmentType.FELLES;
            default -> null; //todo: what to do if segment type is unknown? throw exception or return null?
        };
    }

    private Relasjon mapJsonToRelation(JsonNode jsonRelation) {
        Relasjon relation = new Relasjon();
        relation.setSegmentType(mapSegmentType(jsonRelation.get("SegmentName").asString()));
        relation.setSegmentVerdi(jsonRelation.get("SegmentKey").asString());
        return relation;
    }
}
