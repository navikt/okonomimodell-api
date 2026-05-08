package no.nav.oebs.okonomimodell.service;

import no.nav.oebs.okonomimodell.config.common.logging.OebsResponseHolder;
import no.nav.oebs.okonomimodell.db.repository.SegmentJpaRepository;
import no.nav.oebs.okonomimodell.mapper.JsonToModelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.Segment;
import org.openapitools.model.SegmentType;
import org.openapitools.model.System;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OkonomimodellServiceTest {

    private static final String ARTSKONTO_JSON = """
            {
              "segmentType": "ARTSKONTO",
              "segmentVerdi": "281000000000",
              "beskrivelse": "Pensjonspremie - arbeidsgiverandel til SPK (avsetting)",
              "relasjon": [
                {
                  "segmentType": "STATSREGNSKAPSKONTO",
                  "segmentVerdi": "706500000000"
                }
              ]
            }
            """;

    @Mock
    private SegmentJpaRepository segmentJpaRepository;

    @Mock
    private JsonToModelMapper jsonToModelMapper;

    @Mock
    private OebsResponseHolder oebsResponseHolder;

    @InjectMocks
    private OkonomimodellService okonomimodellService;

    @Test
    void getSegments_shouldReturnMappedSegments() {
        var rawJson = List.of(ARTSKONTO_JSON);
        var expected = List.of(new Segment().segmentType(SegmentType.ARTSKONTO).segmentVerdi("281000000000").beskrivelse("Pensjonspremie - arbeidsgiverandel til SPK (avsetting)"));
        when(segmentJpaRepository.findAllAsJson()).thenReturn(rawJson);
        when(jsonToModelMapper.mapJsonToSegments(rawJson)).thenReturn(expected);

        List<Segment> result = okonomimodellService.getSegments(System.LONN);

        assertEquals(expected, result);
        verify(segmentJpaRepository).findAllAsJson();
        verify(jsonToModelMapper).mapJsonToSegments(rawJson);
    }

    @Test
    void getSegments_shouldSetOebsResponseHolder() {
        var rawJson = List.of(ARTSKONTO_JSON);
        when(segmentJpaRepository.findAllAsJson()).thenReturn(rawJson);
        when(jsonToModelMapper.mapJsonToSegments(rawJson)).thenReturn(List.of());

        okonomimodellService.getSegments(System.LONN);

        verify(oebsResponseHolder).set(rawJson);
    }

    @Test
    void getSegmentsBySegmentType_shouldCallFindBySegmentTypeWhenNoDate() {
        var rawJson = List.of(ARTSKONTO_JSON);
        when(segmentJpaRepository.findBySegmentType("ARTSKONTO")).thenReturn(rawJson);
        when(jsonToModelMapper.mapJsonToSegments(rawJson)).thenReturn(List.of());

        okonomimodellService.getSegmentsBySegmentType(SegmentType.ARTSKONTO, null, System.LONN);

        verify(segmentJpaRepository).findBySegmentType("ARTSKONTO");
        verify(segmentJpaRepository, never()).findByLastUpdatedAndSegmentType(any(), any());
        oebsResponseHolder.clear();
    }

    @Test
    void getSegmentsBySegmentType_shouldCallFindByLastUpdatedWhenDateProvided() {
        var rawJson = List.of(ARTSKONTO_JSON);
        var date = LocalDate.of(2024, 1, 1);
        when(segmentJpaRepository.findByLastUpdatedAndSegmentType("2024-01-01", "ARTSKONTO")).thenReturn(rawJson);
        when(jsonToModelMapper.mapJsonToSegments(rawJson)).thenReturn(List.of());

        okonomimodellService.getSegmentsBySegmentType(SegmentType.ARTSKONTO, date, System.LONN);

        verify(segmentJpaRepository).findByLastUpdatedAndSegmentType("2024-01-01", "ARTSKONTO");
        verify(segmentJpaRepository, never()).findBySegmentType(any());
        oebsResponseHolder.clear();
    }

    @Test
    void getSegmentsBySegmentType_shouldReturnEmptyListWhenNoResults() {
        when(segmentJpaRepository.findBySegmentType("ARTSKONTO")).thenReturn(List.of());
        when(jsonToModelMapper.mapJsonToSegments(List.of())).thenReturn(List.of());

        List<Segment> result = okonomimodellService.getSegmentsBySegmentType(SegmentType.ARTSKONTO, null, System.LONN);

        assertNotNull(result);
        assertEquals(0, result.size());
        oebsResponseHolder.clear();
    }
}
