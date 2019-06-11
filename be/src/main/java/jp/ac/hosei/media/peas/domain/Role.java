package jp.ac.hosei.media.peas.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    Learner, Instructor, Administrator;
    private static Map<Role, GrantedAuthority> authMap; 
    static {
    	authMap = Arrays.stream(values()).collect(Collectors.toMap(r->r, r->new SimpleGrantedAuthority(r.toString())));
    }
    
    public GrantedAuthority toGrantedAuthority() {
    	return authMap.get(this);
    }
}
