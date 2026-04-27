package no.nav.oebs.okonomimodell.config.common.mcd;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class MdcFilter extends OncePerRequestFilter {

	private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		try {
			setCorrelationId(request, response);

			filterChain.doFilter(request, response);
		} finally {
			MdcOperations.remove(MdcOperations.MDC_CORRELATION_ID);
		}
	}

	private void setCorrelationId(HttpServletRequest request, HttpServletResponse response) {
		String correlationId = request.getHeader(CORRELATION_ID_HEADER);
		if (correlationId == null || correlationId.isBlank()) {
			correlationId = MdcOperations.generateCorrelationId();
		}
		MdcOperations.put(MdcOperations.MDC_CORRELATION_ID, correlationId.trim());
		response.setHeader(CORRELATION_ID_HEADER, correlationId.trim());
	}
}

