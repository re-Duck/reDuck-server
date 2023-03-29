package reduck.reduck.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Jwt가 유효성을 검증하는 Filter
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    private boolean validateRequest(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String method = request.getMethod();
        // 로그인
        if (servletPath.equals("/user") && method.equals("POST")) return true;
        // 회원가입
        if (method.equals("/user/{userId}") && method.equals("POST")) return true;
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtAuthenticationFilter.doFilterInternal");
        String servletPath = request.getServletPath();
        String token = jwtProvider.resolveToken(request);
        //로그인 회원가입을 제외한 api사용은 token검증을 거친다.
        if (validateRequest(request)) {
        } else if (token != null && jwtProvider.validateToken(token)) {
            // check access token
            token = token.split(" ")[1].trim();
            Authentication auth = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

        } else {
            if (jwtProvider.isExpireToken(token)) {
                response.setStatus(SC_UNAUTHORIZED);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                response.getWriter().write("ACCESS TOKEN 이 만료되었습니다.");
                new ObjectMapper().writeValue(response.getWriter(), HttpStatus.UNAUTHORIZED);
            }
        }
        filterChain.doFilter(request, response);
    }
}