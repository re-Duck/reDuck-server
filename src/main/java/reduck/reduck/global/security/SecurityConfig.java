package reduck.reduck.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationExceptionFilter jwtExceptionFilter;

    private final String[] permitAllURI = new String[]{"/error", "/login/**", "/user", "/chat", "/auth/email/user/**"};
    private final String[] hasAdminRoleURI = new String[]{"/admin/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> {
                            CorsConfigurationSource source = request -> {
                                CorsConfiguration config = new CorsConfiguration();
                                config.setAllowedOrigins(
                                        List.of("*")
                                );
                                config.setAllowedMethods(
                                        List.of("*")
                                );
                                config.setAllowedHeaders(List.of("*"));
                                return config;
                            };
                            c.configurationSource(source);
                        }
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(multiAntPathRequestMatcher(HttpMethod.POST, permitAllURI)).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.GET.name())).permitAll()
                                .requestMatchers(multiAntPathRequestMatcher(hasAdminRoleURI)).hasRole("ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/user/**")).hasRole("USER")
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(Customizer.withDefaults())
        ;
        return http.build();
    }

    private AntPathRequestMatcher[] multiAntPathRequestMatcher(HttpMethod method, String... paths) {
        return Arrays.stream(paths).map(path ->
                new AntPathRequestMatcher(path, method.name())).toList().toArray(new AntPathRequestMatcher[0]);
    }

    private AntPathRequestMatcher[] multiAntPathRequestMatcher(String... paths) {
        return Arrays.stream(paths).map(
                path -> new AntPathRequestMatcher(path)
        ).toList().toArray(new AntPathRequestMatcher[0]);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}