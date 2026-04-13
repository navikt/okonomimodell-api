package no.nav.oebs.okonomimodell.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Order(1)
public class CorrelationIdFilter extends OncePerRequestFilter {

    static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    static final String MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String correlationId = sanitizeForLog(request.getHeader(CORRELATION_ID_HEADER));
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(MDC_KEY, correlationId);
        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        log.info("{} {} - correlationId={}", request.getMethod(), request.getRequestURI(), correlationId);

        try {
            chain.doFilter(request, response);
        } finally {
            log.info("{} {} {} - correlationId={}", request.getMethod(), request.getRequestURI(), response.getStatus(), correlationId);
            MDC.remove(MDC_KEY);
        }
    }

    private String sanitizeForLog(String value) {
        if (value == null) {
            return null;
        }
        return value.replace('\r', '_')
                .replace('\n', '_')
                .trim();
    }
}
