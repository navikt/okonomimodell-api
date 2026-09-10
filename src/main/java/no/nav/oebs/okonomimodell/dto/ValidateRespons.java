package no.nav.oebs.okonomimodell.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ValidateRespons(
        @JsonProperty("Ccid") String ccid,
        @JsonProperty("ValidateMessage") String validateMessage
) { }
