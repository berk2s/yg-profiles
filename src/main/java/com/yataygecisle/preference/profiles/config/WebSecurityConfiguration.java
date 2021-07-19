package com.yataygecisle.preference.profiles.config;

import com.yataygecisle.commons.security.TokenClaimsConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.Jwt;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable();
        http
                .authorizeRequests()
                    .anyRequest().permitAll()
                .and()
                .oauth2ResourceServer(oauth2 -> oauth2.jwt()
                    .jwtAuthenticationConverter(getJwtAuthenticationConverter()));
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
        return TokenClaimsConverter.converter();
    }

}
