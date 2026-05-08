package no.nav.oebs.okonomimodell.controller;

import no.nav.oebs.okonomimodell.TestApplication;
import no.nav.oebs.okonomimodell.config.common.logging.HttpLoggingFilter;
import no.nav.oebs.okonomimodell.exception.InvalidJsonException;
import no.nav.oebs.okonomimodell.service.OkonomimodellService;
import no.nav.security.mock.oauth2.MockOAuth2Server;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
class OkonomimodellControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockOAuth2Server mockOAuth2Server;

    @MockitoBean
    private OkonomimodellService okonomimodellService;

    @MockitoSpyBean
    private HttpLoggingFilter httpLoggingFilter;

    private String token() {
        return mockOAuth2Server.issueToken("azuread", "test-client", "test-audience").serialize();
    }

    @Test
    void segments_return200WhenSegmentlist() throws Exception {
        when(okonomimodellService.getSegmentsBySegmentType(any(), any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/segmenter/ARTSKONTO?system=LONN")
                        .header("Authorization", "Bearer " + token()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void segments_return500WhenInvalidJson() throws Exception {
        when(okonomimodellService.getSegmentsBySegmentType(any(), any(), any()))
                .thenThrow(new InvalidJsonException("ugyldig JSON"));

        mockMvc.perform(get("/segmenter/ARTSKONTO?system=LONN")
                        .header("Authorization", "Bearer " + token()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void segments_return401WhenMissingToken() throws Exception {
        mockMvc.perform(get("/segmenter"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void segments_return400WhenInvalidDate() throws Exception {
        when(okonomimodellService.getSegmentsBySegmentType(any(), any(), any()))
                .thenThrow(new MethodArgumentTypeMismatchException("lastUpdated", String.class, "lastUpdated", null, new IllegalArgumentException("Ugyldig datoformat")));

        mockMvc.perform(get("/segmenter/ARTSKONTO?oppdatertEtter=20-003-2024")
                        .header("Authorization", "Bearer " + token()))
                .andExpect(status().isBadRequest());
    }
}
