package no.nav.oebs.okonomimodell.service;

import lombok.AllArgsConstructor;
import no.nav.oebs.okonomimodell.model.Kontostreng;
import no.nav.oebs.okonomimodell.config.common.logging.OebsResponseHolder;
import no.nav.oebs.okonomimodell.db.procedure.ValidateKontostrengProcedure;
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

    private final JsonToModelMapper jsonToModelMapper;
    private final OebsResponseHolder oebsResponseHolder;
    private final SegmentJpaRepository segmentJpaRepository;
    private final ValidateKontostrengProcedure kontostrengValidationRepository;

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

    public boolean getKontostrengValidation(String artskonto,
                                            String kostnadssted,
                                            String produkt,
                                            String oppgave,
                                            String felles,
                                            String statskonto,
                                            String kilde,
                                            String tilsagnsaar,
                                            String frittfelt1,
                                            String frittfelt2,
                                            String fullmaktskode,
                                            String regnskapsforer) {
        Kontostreng kontostreng = Kontostreng.of(artskonto, kostnadssted, produkt, oppgave, felles, statskonto, kilde, tilsagnsaar, frittfelt1, frittfelt2, fullmaktskode, regnskapsforer);
        return kontostrengValidationRepository.executeValidateKontostrengProcedure(kontostreng);
    }

}
