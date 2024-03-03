package reduck.reduck.global.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reduck.reduck.global.exception.exception.AuthException;

import java.io.IOException;


/**
 * Jwt가 유효성을 검증하는 Filter
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        String token = jwtProvider.resolveToken(request);

        //로그인 회원가입을 제외한 api사용은 token검증을 거친다.
        if (token != null && jwtProvider.validateToken(token)) {
            // check access token
            token = token.split(" ")[1].trim();
            Authentication auth = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

        } else if (token != null &&jwtProvider.isExpireToken(token)) {
            throw new AuthException("ACCESS TOKEN 이 만료되었습니다.");
        }

        filterChain.doFilter(request, response);
    }
}