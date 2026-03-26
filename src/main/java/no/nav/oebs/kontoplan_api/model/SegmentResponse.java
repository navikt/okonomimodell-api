package no.nav.oebs.kontoplan_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SegmentResponse {

    @JsonProperty("Segment")
    private Segment segment;

    @JsonProperty("SegmentKey")
    private String segmentKey;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("DefaultDimension")
    private List<DefaultDimension> defaultDimension;

    @JsonProperty("ValidFrom")
    private LocalDate validFrom;

    @JsonProperty("ValidTo")
    private LocalDate validTo;
}
