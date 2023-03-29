package reduck.reduck.global.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reduck.reduck.domain.jwt.entity.RefreshToken;
import reduck.reduck.domain.user.entity.Authority;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.util.Utils;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String salt;

    private Key secretKey;

    // 만료시간 : 1Hour
    private final long exp = 1000L * 60;
    // 리프레시 토큰  만료시간 : 14 Day
    private final long refreshExp = 1000L * 60 * 60 * 24 * 14;

    private final JpaUserDetailsService userDetailsService;
    private final Utils utils;
    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    public String refreshAccessToken(HttpServletRequest request, User user ) throws Exception {
        // refreshToken 유효성 검사.
        String refreshToken = resolveToken(request);
        validateToken(refreshToken);

        RefreshToken findRefreshToken = utils.getRefreshToken(user.getId());
        if(!findRefreshToken.equals(refreshToken)){
            //refreshToken  불일치.
            throw new NoSuchElementException("일치하지 않는 refresh token입니다.");
        }

        String userId = getAccount(refreshToken.split(" ")[1].trim());
        return createToken(userId, user.getRoles());
        // 통과시 access token 재발급, 실패시 refresh 토큰 유효성 에러 발생.

    }

    // 토큰 생성
    public String createToken(String account, List<Authority> roles) {
        Claims claims = Jwts.claims().setSubject(account);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String account, List<Authority> roles) {
        Claims claims = Jwts.claims().setSubject(account);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshExp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 권한정보 획득
    // Spring Security 인증과정에서 권한확인을 위한 기능
    public Authentication getAuthentication(String token) {
        System.out.println("JwtProvider.getAuthentication");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccount(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에 담겨있는 유저 account 획득
    public String getAccount(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Authorization Header를 통해 인증을 한다.
    public String resolveToken(HttpServletRequest request) {
        System.out.println("JwtProvider.resolveToken");
        return request.getHeader("Authorization");
    }


    // 토큰 검증
    public boolean validateToken(String token) {
        System.out.println("JwtProvider.validateToken");
        try {
            // Bearer 검증
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println("e = " + e);
            System.out.println("e.getMessage() = " + e.getMessage());
            return false;
        }
    }

    public boolean isExpireToken(String token) {

        try {
            token = token.split(" ")[1].trim();
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return false;
        } catch (ExpiredJwtException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            return true;
        }

    }

}