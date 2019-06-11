package jp.ac.hosei.media.peas.service;

import jp.ac.hosei.media.peas.domain.User;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@ConfigurationProperties(prefix="settings")
@Service
public class AdminLoginUserDetailsService implements UserDetailsService {
    @Setter
    private String adminPassword;
    
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String uid)
            throws UsernameNotFoundException {
        User user = userService.findOne(Long.parseLong(uid));
        if(user != null) {
            user.setPassword(adminPassword);
            return user;
        }

        throw new UsernameNotFoundException("The requested user is not found");        
    }
}
