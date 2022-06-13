package com.infodev.eft_rtgs.security;


import com.infodev.eft_rtgs.pojo.AuthPojo;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Component
@AllArgsConstructor
public class SimpleCORSFilter extends OncePerRequestFilter {

    MyDatabaseUserDetailsService myDatabaseUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Basic ")) {
            response.sendError(401,"Autherization Header Required !!");

            return;
        }
        String token = header.split(" ")[1];
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String decodedString = new String(decodedBytes);


        AuthPojo authPojo = new AuthPojo();
        authPojo.setUsername(decodedString.split(":")[0]);
        authPojo.setPassword(decodedString.split(":")[1]);
        UserDetails userDetails = myDatabaseUserDetailsService.loadUserByUsername(authPojo.getUsername());
        boolean authfailed = false;
        if (userDetails == null) {

            response.sendError(401,"Invalid UserName");
            authfailed = true;
            return;
        } else {
            if (!passwordEncoder.matches(authPojo.getPassword(), userDetails.getPassword())) {

                response.sendError(401,"Invalid Password");
                authfailed = true;
            }
        }

        if (!authfailed) {
            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails == null ?
                            new ArrayList<>() : userDetails.getAuthorities()
            );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }

    }


    @Override
    public void destroy() {
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}