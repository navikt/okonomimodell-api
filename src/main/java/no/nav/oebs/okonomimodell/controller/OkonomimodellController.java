package no.nav.oebs.okonomimodell.controller;

import lombok.AllArgsConstructor;
import no.nav.oebs.okonomimodell.service.OkonomimodellService;
import no.nav.security.token.support.core.api.Protected;
import no.nav.security.token.support.core.api.Unprotected;
import org.openapitools.api.SegmenterApi;
import org.openapitools.model.Segment;
import org.openapitools.model.SegmentType;
import org.openapitools.model.System;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class OkonomimodellController implements SegmenterApi {

    private final OkonomimodellService okonomimodellService;

    @Override
    @Unprotected
    public ResponseEntity<List<Segment>> segments(System system) {
        List<Segment> segments = okonomimodellService.getSegments(system);
        return ResponseEntity.ok(segments);
    }

    @Override
    @Protected
    public ResponseEntity<List<Segment>> segmentsBySegmentType(SegmentType segmenttype, System system) {
        List<Segment> segmentBySegmentType = okonomimodellService.getSegmentsBySegmentType(segmenttype, system);
        return ResponseEntity.ok(segmentBySegmentType);
    }
}
