package no.nav.oebs.okonomimodell.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KonteringsInfoResponse {

    @JsonProperty("OrgId")
    private String orgId;

    @JsonProperty("Segmentnavn")
    private String segmentnavn;

    @JsonProperty("SegmentKey")
    private String segmentKey;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("ValidFrom")
    private LocalDate validFrom;

    @JsonProperty("ValidTo")
    private LocalDate validTo;
}
