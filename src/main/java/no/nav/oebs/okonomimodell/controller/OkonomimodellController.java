package no.nav.oebs.okonomimodell.controller;

import no.nav.oebs.okonomimodell.service.OkonomimodellService;
import org.openapitools.api.SegmenterApi;
import org.openapitools.model.Segment;
import org.openapitools.model.SegmentType;
import org.openapitools.model.System;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OkonomimodellController implements SegmenterApi {

    private final OkonomimodellService okonomimodellService;

    public OkonomimodellController(OkonomimodellService okonomimodellService) {
        this.okonomimodellService = okonomimodellService;
    }

    @Override
    public ResponseEntity<List<Segment>> segmenter(System system) {
        List<Segment> segments = okonomimodellService.getSegments(system);
        return ResponseEntity.ok(segments);
    }
}
