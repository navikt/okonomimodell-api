package no.nav.oebs.okonomimodell.controller;

import no.nav.oebs.okonomimodell.exception.InvalidJsonException;
import no.nav.oebs.okonomimodell.service.OkonomimodellService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.Segment;
import org.openapitools.model.SegmentType;
import org.openapitools.model.System;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OkonomimodellControllerTest {

    @Mock
    private OkonomimodellService okonomimodellService;

    private OkonomimodellController controller;

    @BeforeEach
    void setUp() {
        controller = new OkonomimodellController(okonomimodellService);
    }

    @Nested
    class SegmentsTests {

        @Test
        void segments_returnsOkWithDataFromService() {
            var expected = List.of(new Segment());
            when(okonomimodellService.getSegments(any())).thenReturn(expected);

            var result = controller.segments(System.LONN);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expected, result.getBody());
            verify(okonomimodellService).getSegments(System.LONN);
        }

        @Test
        void segments_propagatesExceptionFromService() {
            when(okonomimodellService.getSegments(any()))
                    .thenThrow(new InvalidJsonException("ugyldig JSON"));

            assertThrows(InvalidJsonException.class, () ->
                    controller.segments(System.LONN));
        }
    }

    @Nested
    class SegmentsBySegmentTypeTests {

        @Test
        void segmentsBySegmentType_returnsOkWithDataFromService() {
            var expected = List.of(new Segment());
            when(okonomimodellService.getSegmentsBySegmentType(any(), any(), any())).thenReturn(expected);

            var result = controller.segmentsBySegmentType(SegmentType.ARTSKONTO, null, System.LONN);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(expected, result.getBody());
            verify(okonomimodellService).getSegmentsBySegmentType(SegmentType.ARTSKONTO, null, System.LONN);
        }

        @Test
        void segmentsBySegmentType_withDate_passesDateToService() {
            when(okonomimodellService.getSegmentsBySegmentType(any(), any(), any())).thenReturn(List.of());
            var date = LocalDate.of(2024, 3, 20);

            controller.segmentsBySegmentType(SegmentType.ARTSKONTO, date, System.LONN);

            verify(okonomimodellService).getSegmentsBySegmentType(SegmentType.ARTSKONTO, date, System.LONN);
        }

        @Test
        void segmentsBySegmentType_propagatesInvalidJsonExceptionFromService() {
            when(okonomimodellService.getSegmentsBySegmentType(any(), any(), any()))
                    .thenThrow(new InvalidJsonException("ugyldig JSON"));

            assertThrows(InvalidJsonException.class, () ->
                    controller.segmentsBySegmentType(SegmentType.ARTSKONTO, null, System.LONN));
        }
    }
}
