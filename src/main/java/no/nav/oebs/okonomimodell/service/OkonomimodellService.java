package no.nav.oebs.okonomimodell.service;

import lombok.AllArgsConstructor;
import no.nav.oebs.okonomimodell.db.repository.SegmentJpaRepository;
import no.nav.oebs.okonomimodell.mapper.JsonToModelMapper;
import org.openapitools.model.Segment;
import org.openapitools.model.SegmentType;
import org.openapitools.model.System;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OkonomimodellService {

    private final SegmentJpaRepository segmentJpaRepository;
    private final JsonToModelMapper jsonToModelMapper;

    public List<Segment> getSegments(System system) {
        return jsonToModelMapper.mapJsonToSegments(
                segmentJpaRepository.findAllAsJson()
        );
    }

    public List<Segment> getSegmentsBySegmentType(SegmentType segmentType, System system) {
        return jsonToModelMapper.mapJsonToSegments(
                segmentJpaRepository.findBySegmentType(segmentType.toString())
        );
    }

}
