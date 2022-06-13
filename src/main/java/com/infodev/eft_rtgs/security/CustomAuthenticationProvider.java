package com.infodev.eft_rtgs.security;


import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {


    private MyDatabaseUserDetailsService myDatabaseUserDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = null;
        try {
             userDetails = myDatabaseUserDetailsService.loadUserByUsername(name);
        }catch (Exception e){
            throw new BadCredentialsException(e.getMessage());
        }
        if (userDetails.getAuthorities() == null) {
            throw new BadCredentialsException("Some Text");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        userDetails.getAuthorities().forEach(e -> {
            authorities.add(new SimpleGrantedAuthority(e.getAuthority()));
        });

        Authentication auth = new UsernamePasswordAuthenticationToken(name, password, authorities);
        return auth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

}