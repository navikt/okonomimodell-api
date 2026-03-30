package no.nav.oebs.kontoplan_api.service;

import lombok.RequiredArgsConstructor;
import no.nav.oebs.kontoplan_api.model.Segment;
import no.nav.oebs.kontoplan_api.model.SegmentResponse;
import no.nav.oebs.kontoplan_api.repository.OkonomimodellRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OkonomimodellService {

    private final OkonomimodellRepository repository;

    public List<SegmentResponse> getSegmenter(Boolean active, String system) {
        List<SegmentResponse> results = repository.findAll();
        return filter(results, active);
    }

    public List<SegmentResponse> getSegmenterByType(Segment segmenttype, Boolean active) {
        List<SegmentResponse> results = repository.findBySegmenttype(segmenttype);
        return filter(results, active);
    }

    private List<SegmentResponse> filter(List<SegmentResponse> results, Boolean active) {
        if (active == null || !active) {
            return results;
        }
        LocalDate today = LocalDate.now();
        return results.stream()
                .filter(s -> s.getValidFrom() != null && !s.getValidFrom().isAfter(today))
                .filter(s -> s.getValidTo() == null || !s.getValidTo().isBefore(today))
                .toList();
    }
}
