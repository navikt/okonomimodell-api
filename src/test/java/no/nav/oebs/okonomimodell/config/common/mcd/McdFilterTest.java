package no.nav.oebs.okonomimodell.config.common.mcd;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class McdFilterTest {

    @Mock
    private FilterChain filterChain;

    private MdcFilter mdcFilter;

    private static final String CORRELATION_ID_HEADER = MdcFilter.CORRELATION_ID_HEADER;
    
    @BeforeEach
    void setUp() {
        mdcFilter = new MdcFilter();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void doFilterInternal_shouldCallFilterChain() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        mdcFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(any(), any());
    }

    @Test
    void doFilterInternal_shouldSetCorrelationIdInMdcDuringRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        doAnswer(invocation -> {
            assertNotNull(MDC.get(MdcOperations.MDC_CORRELATION_ID));
            return null;
        }).when(filterChain).doFilter(any(), any());

        mdcFilter.doFilterInternal(request, response, filterChain);
    }

    @Test
    void doFilterInternal_shouldUseIncomingCorrelationIdWhenHeaderIsPresent() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CORRELATION_ID_HEADER, "existing-correlation-id");
        MockHttpServletResponse response = new MockHttpServletResponse();

        doAnswer(invocation -> {
            assertEquals("existing-correlation-id", MDC.get(MdcOperations.MDC_CORRELATION_ID));
            return null;
        }).when(filterChain).doFilter(any(), any());

        mdcFilter.doFilterInternal(request, response, filterChain);

        assertEquals("existing-correlation-id", response.getHeader(CORRELATION_ID_HEADER));
    }

    @Test
    void doFilterInternal_shouldSetGeneratedCorrelationIdInResponseHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        doAnswer(invocation -> {
            assertEquals(MDC.get(MdcOperations.MDC_CORRELATION_ID), response.getHeader(CORRELATION_ID_HEADER));
            return null;
        }).when(filterChain).doFilter(any(), any());

        mdcFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(response.getHeader(CORRELATION_ID_HEADER));
    }

    @Test
    void doFilterInternal_shouldRemoveCorrelationIdFromMdcAfterRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        mdcFilter.doFilterInternal(request, response, filterChain);

        assertNull(MDC.get(MdcOperations.MDC_CORRELATION_ID));
    }

    @Test
    void doFilterInternal_shouldSanitizeCorrelationIdContainingControlChars() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CORRELATION_ID_HEADER, "valid-id\r\nX-Injected: evil");
        MockHttpServletResponse response = new MockHttpServletResponse();

        mdcFilter.doFilterInternal(request, response, filterChain);

        String returned = response.getHeader(CORRELATION_ID_HEADER);
        assertNotNull(returned);
        assertFalse(returned.contains("\r"));
        assertFalse(returned.contains("\n"));
    }

    @Test
    void doFilterInternal_shouldTruncateOversizedCorrelationId() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CORRELATION_ID_HEADER, "a".repeat(256));
        MockHttpServletResponse response = new MockHttpServletResponse();

        mdcFilter.doFilterInternal(request, response, filterChain);

        String returned = response.getHeader(CORRELATION_ID_HEADER);
        assertNotNull(returned);
        assertTrue(returned.length() <= 128);
    }

    @Test
    void doFilterInternal_shouldGenerateNewIdWhenHeaderContainsOnlyControlChars() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CORRELATION_ID_HEADER, "\r\n");
        MockHttpServletResponse response = new MockHttpServletResponse();

        mdcFilter.doFilterInternal(request, response, filterChain);

        String returned = response.getHeader(CORRELATION_ID_HEADER);
        assertNotNull(returned);
        assertFalse(returned.contains("\r"));
        assertFalse(returned.contains("\n"));
    }

    @Test
    void doFilterInternal_shouldRemoveCorrelationIdEvenWhenFilterChainThrows() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        doThrow(new RuntimeException("noe gikk galt")).when(filterChain).doFilter(any(), any());

        assertThrows(RuntimeException.class, () ->
                mdcFilter.doFilterInternal(request, response, filterChain));

        assertNull(MDC.get(MdcOperations.MDC_CORRELATION_ID));
    }
}
