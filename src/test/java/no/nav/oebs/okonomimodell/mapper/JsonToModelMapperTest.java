package no.nav.oebs.okonomimodell.mapper;

import no.nav.oebs.okonomimodell.exception.InvalidJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Segment;
import org.openapitools.model.SegmentType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonToModelMapperTest {

    private JsonToModelMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new JsonToModelMapper();
    }

    @Test
    void mapJsonStringToSegment_shouldMapSingleObject() {
        String json = """
                {
                "segmentType": "KOSTNADSSTED",
                "segmentVerdi": "857410",
                "beskrivelse": "Infrastruktur",
                "relasjon": [
                ]
                }
                """;

        Segment segment = mapper.mapJsonStringToSegment(json);

        assertEquals(SegmentType.KOSTNADSSTED, segment.getSegmentType());
        assertEquals("857410", segment.getSegmentVerdi());
        assertEquals("Infrastruktur", segment.getBeskrivelse());
        assertEquals(0, segment.getRelasjon().size());
    }

    @Test
    void mapJsonStringToSegment_shouldMapArrayAndUseFirstElement() {
        String json = """
                [
                 {
                 "segmentType": "KOSTNADSSTED",
                 "segmentVerdi": "857630",
                 "beskrivelse": "Virksomhetsteknologi",
                 "relasjon": [
                 ]
                 }
                 ]
                """;

        Segment segment = mapper.mapJsonStringToSegment(json);

        assertEquals(SegmentType.KOSTNADSSTED, segment.getSegmentType());
        assertEquals("857630", segment.getSegmentVerdi());
        assertEquals("Virksomhetsteknologi", segment.getBeskrivelse());
    }

    @Test
    void mapJsonStringToSegment_shouldMapRelations() {
        String json = """
                {
                  "segmentType": "ARTSKONTO",
                  "segmentVerdi": "281000000000",
                  "beskrivelse": "Pensjonspremie",
                  "relasjon": [
                    {
                      "segmentType": "STATSREGNSKAPSKONTO",
                      "segmentVerdi": "706500000000"
                    }
                  ]
                }
                """;

        Segment segment = mapper.mapJsonStringToSegment(json);

        assertNotNull(segment.getRelasjon());
        assertEquals(1, segment.getRelasjon().size());
        assertEquals(SegmentType.STATSREGNSKAPSKONTO, segment.getRelasjon().get(0).getSegmentType());
        assertEquals("706500000000", segment.getRelasjon().get(0).getSegmentVerdi());
    }

    @Test
    void mapJsonStringToSegment_shouldThrowExceptionWhenSegmentTypeUnknown() {
        String json = """
                {
                "segmentType": "NON_EXISTENT_TYPE",
                "segmentVerdi": "857410",
                "beskrivelse": "Infrastruktur",
                "relasjon": [
                ]
                }
                """;

        assertThrows(InvalidJsonException.class, () -> mapper.mapJsonStringToSegment(json));
    }

    @Test
    void mapJsonStringToSegment_shouldThrowExceptionForInvalidJson() {
        String ugyldigJson = "dette er ikke json";

        assertThrows(InvalidJsonException.class, () -> mapper.mapJsonStringToSegment(ugyldigJson));
    }

    @Test
    void mapJsonToSegments_shouldMapList() {
        String json1 = """
                {
                 "segmentType": "ARTSKONTO",
                 "segmentVerdi": "857630",
                 "beskrivelse": "Virksomhetsteknologi",
                 "relasjon": [
                 ]
                 }
              """;
        String json2 = """
                {
                "segmentType": "KOSTNADSSTED",
                "segmentVerdi": "857410",
                "beskrivelse": "Infrastruktur",
                "relasjon": [
                ]
                }
                """;

        List<Segment> segmenter = mapper.mapJsonToSegments(List.of(json1, json2));

        assertEquals(2, segmenter.size());
        assertEquals(SegmentType.ARTSKONTO, segmenter.get(0).getSegmentType());
        assertEquals(SegmentType.KOSTNADSSTED, segmenter.get(1).getSegmentType());
    }

    @Test
    void mapJsonToSegments_shouldReturnEmptyListForEmptyInput() {
        List<Segment> segmenter = mapper.mapJsonToSegments(List.of());

        assertTrue(segmenter.isEmpty());
    }
}
