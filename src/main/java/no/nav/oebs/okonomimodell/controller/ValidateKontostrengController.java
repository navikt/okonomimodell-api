package no.nav.oebs.okonomimodell.controller;

import lombok.AllArgsConstructor;
import no.nav.oebs.okonomimodell.service.OkonomimodellService;
import no.nav.security.token.support.core.api.Unprotected;
import org.jspecify.annotations.Nullable;
import org.openapitools.api.ValidatekontostrengApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ValidateKontostrengController implements ValidatekontostrengApi {

    private final OkonomimodellService okonomimodellService;

    @Override
    @Unprotected
    public ResponseEntity<Boolean> validateKontostreng(@Nullable String artskonto, @Nullable String kostnadssted, @Nullable String produkt, @Nullable String oppgave, @Nullable String felles, @Nullable String statskonto, @Nullable String kilde, @Nullable String tilsagnsaar, @Nullable String frittfelt1, @Nullable String frittfelt2, @Nullable String fullmaktskode, @Nullable String regnskapsforer) {
        return ResponseEntity.ok(okonomimodellService.getKontostrengValidation(artskonto, kostnadssted, produkt, oppgave, felles, statskonto, kilde, tilsagnsaar, frittfelt1, frittfelt2, fullmaktskode, regnskapsforer));
    }

}
