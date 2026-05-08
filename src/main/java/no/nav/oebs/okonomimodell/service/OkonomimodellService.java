package no.nav.oebs.okonomimodell.service;

import lombok.AllArgsConstructor;
import no.nav.oebs.okonomimodell.config.common.logging.OebsResponseHolder;
import no.nav.oebs.okonomimodell.db.repository.SegmentJpaRepository;
import no.nav.oebs.okonomimodell.mapper.JsonToModelMapper;
import org.openapitools.model.Segment;
import org.openapitools.model.SegmentType;
import org.openapitools.model.System;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class OkonomimodellService {

    private final SegmentJpaRepository segmentJpaRepository;
    private final JsonToModelMapper jsonToModelMapper;
    private final OebsResponseHolder oebsResponseHolder;

    public List<Segment> getSegments(System system) {
        var raw = segmentJpaRepository.findAllAsJson();
        oebsResponseHolder.set(raw);
        return jsonToModelMapper.mapJsonToSegments(raw);
    }

    public List<Segment> getSegmentsBySegmentType(SegmentType segmentType, LocalDate lastUpdated, System system) {
        var segments = lastUpdated != null
                ? segmentJpaRepository.findByLastUpdatedAndSegmentType(lastUpdated.toString(), segmentType.toString())
                : segmentJpaRepository.findBySegmentType(segmentType.toString());
        oebsResponseHolder.set(segments);
        return jsonToModelMapper.mapJsonToSegments(segments);
    }

}
