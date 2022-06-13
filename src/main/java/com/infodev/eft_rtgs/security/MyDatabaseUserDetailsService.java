package com.infodev.eft_rtgs.security;

import com.infodev.eft_rtgs.Model.userManagement.User;
import com.infodev.eft_rtgs.view.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyDatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUserName(username);
        if(user==null){
           return null;
        }
        List<SimpleGrantedAuthority> grantedAuthorities =
                user.getRoles().stream().map(authority -> new SimpleGrantedAuthority(authority.getRoleName())).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), grantedAuthorities);
    }

}
