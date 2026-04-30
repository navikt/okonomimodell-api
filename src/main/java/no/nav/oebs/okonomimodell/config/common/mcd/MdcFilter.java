package no.nav.oebs.okonomimodell.config.common.mcd;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class MdcFilter extends OncePerRequestFilter {

	static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
	private static final int CORRELATION_ID_MAX_LENGTH = 128;
	private static final String CONTROL_CHARS_PATTERN = "[\\p{Cntrl}]";

	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
			@NotNull FilterChain filterChain)
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
		} else {
			correlationId = sanitize(correlationId.trim());
		}
		MdcOperations.put(MdcOperations.MDC_CORRELATION_ID, correlationId);
		response.setHeader(CORRELATION_ID_HEADER, correlationId);
	}

	private String sanitize(String value) {
		String sanitized = value.replaceAll(CONTROL_CHARS_PATTERN, "");
		if (sanitized.length() > CORRELATION_ID_MAX_LENGTH) {
			sanitized = sanitized.substring(0, CORRELATION_ID_MAX_LENGTH);
		}
		if (sanitized.isBlank()) {
			return MdcOperations.generateCorrelationId();
		}
		return sanitized;
	}
}

