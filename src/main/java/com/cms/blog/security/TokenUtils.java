package com.cms.blog.security;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class TokenUtils {

    public String generateToken(String username, Set<Role> roles){
        
        JwtClaimsBuilder claimsBuilder = Jwt.claims();

        Set<String> groups = new HashSet<>();
        for(Role role: roles) groups.add(role.toString());
        

        claimsBuilder.upn(username);
        claimsBuilder.groups(groups);
        return claimsBuilder.sign();
    }
    
}
