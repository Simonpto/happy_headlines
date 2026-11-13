package simon.monitoring;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/actuator");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        long start = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("{} {} failed after {} ms", request.getMethod(), request.getRequestURI(), System.currentTimeMillis() - start, e);
            throw e;
        }
        long duration = System.currentTimeMillis() - start;
        int status = response.getStatus();
        String message = "{} {} -> {} in {} ms";
        if (status >= 500) {
            log.error(message, request.getMethod(), request.getRequestURI(), status, duration);
        } else if (status >= 400) {
            log.warn(message, request.getMethod(), request.getRequestURI(), status, duration);
        } else {
            log.info(message, request.getMethod(), request.getRequestURI(), status, duration);
        }
    }
}
