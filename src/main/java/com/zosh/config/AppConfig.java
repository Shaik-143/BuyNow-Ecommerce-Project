package com.zosh.config;
/*import java.lang.reflect.Array;*/
import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(Authorize -> Authorize
//                		.requestMatchers("/api/admin/**").hasAnyRole("SHOP_OWNER","ADMIN")
                                .requestMatchers("/api/**").authenticated()
                                .requestMatchers("/api/products/*/reviews").permitAll()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
               
		return http.build();	
	}
    // CORS Configuration
    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(Arrays.asList("https://zosh-bazzar-zosh.vercel.app", "http://localhost:3000"));
                cfg.setAllowedMethods(Collections.singletonList("*"));// all methods get,post,put,delete,patch
                cfg.setAllowCredentials(true);
                cfg.setAllowedHeaders(Collections.singletonList("*")); // headers are allowed
                cfg.setExposedHeaders(Arrays.asList("Authorization")); // It tells the browser that allowed frontend to see the "Authorization" header
                cfg.setMaxAge(3600L); // timer to logged in for 1 hrs
                return cfg;
            }
        };
    }
    @Bean
    PasswordEncoder passwordEncoder() {// This ensures that when you save a user's password, 
    	//it is hashed (turned into a scrambled string) so that even if your database is hacked, the real passwords are safe.
		return new BCryptPasswordEncoder();
	}

    @Bean
    public RestTemplate restTemplate() {// A tool that allows your Spring Boot app to make requests to other APIs
    	//(like a weather API or a payment gateway).
        return new RestTemplate();
    }
}