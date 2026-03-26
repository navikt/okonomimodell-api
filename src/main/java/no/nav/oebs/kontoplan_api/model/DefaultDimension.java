package no.nav.oebs.kontoplan_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultDimension {

    @JsonProperty("Segment")
    private Segment segment;

    @JsonProperty("SegmentKey")
    private String segmentKey;
}
