package uz.car.turbo.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] METHODS = {"GET", "POST", "PUT", "DELETE"};
    private final String[] HEADERS = {"accept", "cache-Control", "authorization", "content-type", "x-auth-token", "cookie", "set-cookie",
            "content-disposition", "dnt", "if-modified-since", "keep-alive", "origin", "user-agent", "x-requested-with"};

    public static String[] WHITELIST = {
            "/api/login"
            , "/api/v1/category/get/**"
            , "/api/v1/category/all"
            , "/api/v1/colleges/get/**"
            , "/api/v1/colleges/all"
            , "/api/v1/product/get/**"
            , "/api/v1/product/all/**"
            , "/api/v1/project/get/**"
            , "/api/v1/project/all/"
            , "/api/v1/auth/login"
            , "/swagger-ui/**"
    };

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Arrays.asList(HEADERS));
        configuration.setAllowedMethods(Arrays.asList(METHODS));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().cors();
        http
                .authorizeRequests()
                .antMatchers(WHITELIST)
                .permitAll()
                .anyRequest()
                .authenticated();
        http
                .addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
