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
        String servletPath = request.getServletPath();

        System.out.println("filterChain = " + filterChain.getClass());
        System.out.println("servletPath = " + servletPath);
        String token = jwtProvider.resolveToken(request);
        String method = request.getMethod();
        //로그인 회원가입을 제외한 api사용은 token검증을 거친다.
        if ((servletPath.equals("/user/{userId}") && method.equals("POST"))
                || (servletPath.equals("/user") && method.equals("POST"))) {

        }
        else if (token != null && jwtProvider.validateToken(token)) {
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