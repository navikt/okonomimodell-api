package no.nav.oebs.okonomimodell.config.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import no.nav.oebs.okonomimodell.repository.entity.KallLogg;
import no.nav.oebs.okonomimodell.repository.KallLoggJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpLoggingFilterTest {

    @Mock
    private KallLoggJpaRepository kallLoggJpaRepository;

    @Mock
    private FilterChain filterChain;

    @Mock
    private OebsResponseHolder oebsResponseHolder;

    private HttpLoggingFilter httpLoggingFilter;

    @BeforeEach
    void setUp() {
        httpLoggingFilter = new HttpLoggingFilter(kallLoggJpaRepository, oebsResponseHolder);
    }

    @Test
    void doFilterInternal_shouldCallFilterChain() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/segmenter");
        MockHttpServletResponse response = new MockHttpServletResponse();

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(any(), any());
    }

    @Test
    void doFilterInternal_shouldSaveKallLogg() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/segmenter");
        request.setParameter("system", "LONN");
        String correlationId = UUID.randomUUID().toString();
        request.addHeader("x-correlation-id", correlationId);
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(200);

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
        verify(kallLoggJpaRepository, times(1)).save(captor.capture());

        KallLogg savedLogg = captor.getValue();
        assertEquals("GET", savedLogg.getMethod());
        assertEquals("/segmenter", savedLogg.getOperation());
        assertEquals(200, savedLogg.getStatus());
        assertEquals(KallLogg.TYPE_REST, savedLogg.getType());
        assertEquals(KallLogg.RETNING_INN, savedLogg.getKallRetning());
        assertEquals("LONN", savedLogg.getLogginfo());
        assertEquals(correlationId, savedLogg.getKorrelasjonId());
    }

    @Test
    void doFilterInternal_shouldSaveNullCorrelationIdWhenMissing() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/segmenter");
        MockHttpServletResponse response = new MockHttpServletResponse();

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
        verify(kallLoggJpaRepository, times(1)).save(captor.capture());

        KallLogg savedLogg = captor.getValue();
        assertEquals("null", savedLogg.getKorrelasjonId());
    }

    @Test
    void doFilterInternal_shouldSaveNullCorrelationIdWhenInvalid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/segmenter");
        request.addHeader("x-correlation-id", " ");
        MockHttpServletResponse response = new MockHttpServletResponse();

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
        verify(kallLoggJpaRepository, times(1)).save(captor.capture());

        KallLogg savedLogg = captor.getValue();
        assertEquals("null", savedLogg.getKorrelasjonId());
    }

    @Test
    void doFilterInternal_shouldSaveNullCorrelationIdWhenHeaderIsNotUuid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/segmenter");
        request.addHeader("x-correlation-id", "corr-123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        httpLoggingFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<KallLogg> captor = ArgumentCaptor.forClass(KallLogg.class);
        verify(kallLoggJpaRepository, times(1)).save(captor.capture());

        KallLogg savedLogg = captor.getValue();
        assertEquals("null", savedLogg.getKorrelasjonId());
    }

    @Test
    void doFilterInternal_shouldSaveKallLoggEvenWhenFilterChainThrows() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/segmenter");
        MockHttpServletResponse response = new MockHttpServletResponse();

        doThrow(new RuntimeException("noe gikk galt")).when(filterChain).doFilter(any(), any());

        assertThrows(RuntimeException.class, () ->
                httpLoggingFilter.doFilterInternal(request, response, filterChain));

        verify(kallLoggJpaRepository, times(1)).save(any());
    }

    @Test
    void doFilterInternal_shouldNotThrowWhenSaveKallLoggFails() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/segmenter");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(kallLoggJpaRepository.save(any())).thenThrow(new RuntimeException("DB utilgjengelig"));

        assertDoesNotThrow(() ->
                httpLoggingFilter.doFilterInternal(request, response, filterChain));
    }

    @Test
    void shouldNotFilter_shouldSkipActuatorPaths() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/actuator/health");
        request.setServletPath("/actuator/health");

        assertTrue(httpLoggingFilter.shouldNotFilter(request));
    }

    @Test
    void shouldNotFilter_shouldNotSkipNonActuatorPaths() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/segmenter");
        request.setServletPath("/segmenter");

        assertFalse(httpLoggingFilter.shouldNotFilter(request));
    }
}
