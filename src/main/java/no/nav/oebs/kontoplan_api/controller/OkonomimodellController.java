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

@Slf4j
@RestController
@RequestMapping("/api/v1/okonomimodell")
@RequiredArgsConstructor
@Tag(name = "Økonomimodell", description = "Segmenter i økonomimodellen fra OeBS")
public class OkonomimodellController {

    @GetMapping("/segmenter")
    @Operation(summary = "Hent alle segmenter", description = "Returnerer alle segmenter, med valgfri filtrering på aktive og system")
    public ResponseEntity<List<SegmentResponse>> getSegmenter(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String system) {

        log.info("Henter segmenter - active={}, system={}", active, system);
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/segmenter/{segmenttype}")
    @Operation(summary = "Hent segmenter for type", description = "Returnerer segmenter for angitt segmenttype")
    public ResponseEntity<List<SegmentResponse>> getSegmenterByType(
            @PathVariable Segment segmenttype,
            @RequestParam(required = false) Boolean active) {

        log.info("Henter segmenter for type={} - active={}", segmenttype, active);
        return ResponseEntity.ok(List.of());
    }
}
