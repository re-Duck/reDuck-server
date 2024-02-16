package reduck.reduck.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reduck.reduck.global.security.CustomAuthenticationPrincipalArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")   // 추후 frontend 배포 ip 추가하기
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(3600); //get,post,patch 등 모든 허용할 HTTP method정의
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(customAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public CustomAuthenticationPrincipalArgumentResolver customAuthenticationPrincipalArgumentResolver() {
        return new CustomAuthenticationPrincipalArgumentResolver();
    }
}