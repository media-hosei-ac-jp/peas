package jp.ac.hosei.media.peas.config;

import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true)
@EnableWebSecurity
@Configuration
@ConfigurationProperties(prefix="settings")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Setter
	private String csrfHeaderName;
	@Setter
	private String frontendUrl;
	@Setter
	private String adminPassword;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http		
		.authorizeRequests()

		.antMatchers("/lti/launch","/ws", "/")
		.permitAll()
		
		 //guestReview用に追加
		//saveReview getTarget getQuizByTargetId
		.antMatchers("/api/target/guest/**", "/api/review/guest/**", "/api/quiz/guest/**") 
		.permitAll()
		
		.anyRequest()
		.authenticated()

//		.and().logout()
//		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//		.logoutSuccessUrl(frontendUrl)
		
		//for lti launch //popup起動できればいらない 
//		.and()
//		.headers().frameOptions().disable()
		
		.and()
		.csrf().disable(); //debug
	}
	
	@Bean //remove ROLE_ prefix
	GrantedAuthorityDefaults grantedAuthorityDefaults() {
	    return new GrantedAuthorityDefaults("");
	}
}

