package no.nav.oebs.okonomimodell.controller;

import no.nav.oebs.okonomimodell.TestApplication;
import no.nav.oebs.okonomimodell.config.common.logging.HttpLoggingFilter;
import no.nav.oebs.okonomimodell.exception.InvalidJsonException;
import no.nav.oebs.okonomimodell.service.OkonomimodellService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @MockitoBean
    private OkonomimodellService okonomimodellService;

    @MockitoSpyBean
    private HttpLoggingFilter httpLoggingFilter;

    @Test
    void segments_returnerer200MedSegmentliste() throws Exception {

        when(okonomimodellService.getSegments(any())).thenReturn(List.of());

        mockMvc.perform(get("/segmenter?system=LONN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void segments_returnerer500VedInvalidJson() throws Exception {
        when(okonomimodellService.getSegments(any()))
                .thenThrow(new InvalidJsonException("ugyldig JSON"));

        mockMvc.perform(get("/segmenter?system=OB"))
                .andExpect(status().isInternalServerError());
    }
}
