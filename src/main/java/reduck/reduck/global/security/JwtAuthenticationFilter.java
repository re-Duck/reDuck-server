package reduck.reduck.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtAuthenticationFilter.doFilterInternal");
        System.out.println("filterChain = " + filterChain.getClass());
        String token = jwtProvider.resolveToken(request);
        String servletPath = request.getServletPath();
        String method = request.getMethod();
        System.out.println("method = " + method);
        System.out.println("servletPath = " + servletPath);
        if(servletPath.equals("login"))
        if (token != null && jwtProvider.validateToken(token)) {
            // check access token
            System.out.println("token = " + token);
            token = token.split(" ")[1].trim();
            System.out.println("token = " + token);
            Authentication auth = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
//        else if (!jwtProvider.tokenExp(token)) {
//            //토큰 만료.
//            System.out.println("토큰 만료.");
//            response.setStatus(SC_UNAUTHORIZED);
//            response.setContentType(APPLICATION_JSON_VALUE);
//            response.setCharacterEncoding("utf-8");
//            response.getWriter().write("ACCESS TOKEN 이 만료되었습니다.");
//            new ObjectMapper().writeValue(response.getWriter(), HttpStatus.UNAUTHORIZED);
//        }
//        else {
//            //토큰 만료.
//            System.out.println("토큰 만료.");
//            response.setStatus(SC_UNAUTHORIZED);
//            response.setContentType(APPLICATION_JSON_VALUE);
//            response.setCharacterEncoding("utf-8");
//            response.getWriter().write("ACCESS TOKEN 이 만료되었습니다.");
//            new ObjectMapper().writeValue(response.getWriter(), HttpStatus.UNAUTHORIZED);
//        }

        filterChain.doFilter(request, response);
    }
}