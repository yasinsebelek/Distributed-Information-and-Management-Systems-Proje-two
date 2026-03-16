package sau.com.schoolmanagement1.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger log =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    private static final String REQUEST_ID = "requestId";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestId = httpRequest.getHeader("X-Request-Id");

        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        long startTime = System.currentTimeMillis();

        MDC.put(REQUEST_ID, requestId);

        try {
            log.info("REQUEST START | method={} | uri={}",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI());

            chain.doFilter(request, response);

            long duration = System.currentTimeMillis() - startTime;

            log.info("REQUEST END | method={} | uri={} | status={} | duration={} ms",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI(),
                    httpResponse.getStatus(),
                    duration);

        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;

            log.error("REQUEST FAILED | method={} | uri={} | duration={} ms | message={}",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI(),
                    duration,
                    ex.getMessage(),
                    ex);

            throw ex;

        } finally {
            MDC.clear();
        }
    }
}