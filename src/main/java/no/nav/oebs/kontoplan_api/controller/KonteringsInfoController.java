package no.nav.oebs.kontoplan_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.oebs.kontoplan_api.model.KonteringsInfoResponse;
import no.nav.oebs.kontoplan_api.service.KonteringsInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/konteringsinfo")
@RequiredArgsConstructor
@Tag(name = "Konteringsinfo", description = "Accounting info for payroll systems")
public class KonteringsInfoController {

    private final KonteringsInfoService service;

    @GetMapping
    @Operation(
            summary = "Get konteringsinfo",
            description = "Returns active konteringsinfo records for a given org, segment name and system"
    )
    public ResponseEntity<List<KonteringsInfoResponse>> getKonteringsInfo(
            @RequestParam String org_id,
            @RequestParam String segmentnavn,
            @RequestParam String system) {

        log.info("Fetching konteringsinfo - org_id={}, segmentnavn={}, system={}", org_id, segmentnavn, system);
        return ResponseEntity.ok(service.getKonteringsInfo(org_id, segmentnavn, system));
    }
}
