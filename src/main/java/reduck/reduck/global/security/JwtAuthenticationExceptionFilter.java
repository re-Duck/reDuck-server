package reduck.reduck.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reduck.reduck.global.exception.ErrorResponse;
import reduck.reduck.global.exception.exception.AuthException;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            log.error(e.getMessage());
            response.setStatus(e.getErrorCode().getHttpStatus().value());
            response.setContentType("application/json; charset=UTF8");

            response.getWriter().write(
                    ErrorResponse.builder()
                            .code(e.getErrorCode().name())
                            .status(e.getErrorCode().getHttpStatus().value())
                            .message(e.getHandleMessage())
                            .build()
                            .convertToJson()
            );
        }
    }
}
