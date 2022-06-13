package com.infodev.eft_rtgs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter  {
    private final SimpleCORSFilter simpleCORSFilter;

MyDatabaseUserDetailsService myDatabaseUserDetailsService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
                .antMatchers("/api/v1/register").permitAll()
                .antMatchers("/api/document/temp/*").permitAll()
                .antMatchers("/api/document/doc/*").permitAll()
                .and()

                .authorizeRequests().anyRequest().authenticated().and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint( unauthorizedHandler())
               .accessDeniedHandler(accessDeniedHandler());
        http.addFilterBefore(
                simpleCORSFilter,
                UsernamePasswordAuthenticationFilter.class
        );
    }


    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new CustomAuthenticationProvider(myDatabaseUserDetailsService));
    }

    @Bean
    RestAccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }
   @Bean
   AuthenticationEntryPoint unauthorizedHandler() {
     //   return new RestAuthenticationEntryPoint();
       return new AuthenticationEntryPoint() {

           @Override
           public void commence(HttpServletRequest request, HttpServletResponse response,
                                AuthenticationException authException) throws IOException, ServletException {
if(response.containsHeader("error")) {
    Map<String, String> rsp = new HashMap<>();

    response.setStatus(HttpStatus.UNAUTHORIZED.value());

    rsp.put("resp_code", HttpStatus.UNAUTHORIZED.value() + "");
    rsp.put("resp_msg", response.getHeader("error"));

    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(rsp));
    response.getWriter().flush();
    response.getWriter().close();
}
           }
       };
    }
}
