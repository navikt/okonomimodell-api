package no.nav.oebs.okonomimodell.service;

import no.nav.oebs.okonomimodell.repository.OkonomimodellRepository;
import org.openapitools.model.Segment;
import org.openapitools.model.SegmentType;
import org.openapitools.model.System;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OkonomimodellService {

    private final OkonomimodellRepository repository;

    public OkonomimodellService(OkonomimodellRepository repository) {
        this.repository = repository;
    }
    public List<Segment> getSegments(System system, SegmentType segmentType) {
        return repository.findAll();
    }

}
