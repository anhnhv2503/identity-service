package com.anhnhvcoder.devteria.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers(HttpMethod.POST, "/v2/users/sign-up").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/introspect").permitAll()
                        .anyRequest().authenticated()
        );

        http.oauth2ResourceServer(resourceServer ->
                resourceServer.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())));

        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder(){

        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");

       return NimbusJwtDecoder
               .withSecretKey(secretKeySpec)
               .macAlgorithm(MacAlgorithm.HS512)
               .build();
    }
}
