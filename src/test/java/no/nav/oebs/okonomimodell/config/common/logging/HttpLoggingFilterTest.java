package no.nav.oebs.okonomimodell.config.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import no.nav.oebs.okonomimodell.db.entity.KallLogg;
import no.nav.oebs.okonomimodell.db.repository.KallLoggJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpLoggingFilterTest {

    @Mock
    private KallLoggJpaRepository kallLoggJpaRepository;

    @Mock
    private FilterChain filterChain;

    private HttpLoggingFilter httpLoggingFilter;

    @BeforeEach
    void setUp() {
        httpLoggingFilter = new HttpLoggingFilter(kallLoggJpaRepository);
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
}
