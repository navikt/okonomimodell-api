package no.nav.oebs.kontoplan_api.service;

import lombok.RequiredArgsConstructor;
import no.nav.oebs.kontoplan_api.model.KonteringsInfoResponse;
import no.nav.oebs.kontoplan_api.repository.KonteringsInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KonteringsInfoService {

    private final KonteringsInfoRepository repository;

    public List<KonteringsInfoResponse> getKonteringsInfo(String orgId, String segmentnavn, String system) {
        return repository.findActive(orgId, segmentnavn, system);
    }
}
