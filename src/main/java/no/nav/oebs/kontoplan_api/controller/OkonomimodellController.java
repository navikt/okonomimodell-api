package no.nav.oebs.kontoplan_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.oebs.kontoplan_api.model.Segment;
import no.nav.oebs.kontoplan_api.model.SegmentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import no.nav.oebs.kontoplan_api.service.OkonomimodellService;

@Slf4j
@RestController
@RequestMapping("/api/v1/okonomimodell")
@RequiredArgsConstructor
@Tag(name = "Okonomimodell", description = "Segments in the OeBS accounting model")
public class OkonomimodellController {

    private final OkonomimodellService service;

    @GetMapping("/segmenter")
    @Operation(summary = "Get all segments", description = "Returns all segments, with optional filtering on active and system")
    public ResponseEntity<List<SegmentResponse>> getSegmenter(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String system) {

        log.info("Fetching segments - active={}, system={}", active, system);
        return ResponseEntity.ok(service.getSegmenter(active, system));
    }

    @GetMapping("/segmenter/{segmenttype}")
    @Operation(summary = "Get segments by type", description = "Returns segments for the given segment type")
    public ResponseEntity<List<SegmentResponse>> getSegmenterByType(
            @PathVariable Segment segmenttype,
            @RequestParam(required = false) Boolean active) {

        log.info("Fetching segments for type={} - active={}", segmenttype, active);
        return ResponseEntity.ok(service.getSegmenterByType(segmenttype, active));
    }
}
