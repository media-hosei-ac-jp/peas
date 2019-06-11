package jp.ac.hosei.media.peas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jp.ac.hosei.media.peas.domain.User;
import lombok.Setter;

@Service
@ConfigurationProperties(prefix="settings")
public class AuthService {
	@Autowired
	private AuthenticationManager authManager;

	@Setter
	private String adminPassword;

	/**
	 * ログイン処理を行います.
	 * @return
	 */
	public boolean login(long id, String password) {
		//Spring Security認証処理
		Authentication authResult = null;
		try{
			Authentication request = new UsernamePasswordAuthenticationToken(id, password);
			authResult = authManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(authResult);

			return authResult.isAuthenticated();
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean adminLogin(long id) {
		return login(id, adminPassword);
	}

	public void authUser(User user) {
		Authentication token = new UsernamePasswordAuthenticationToken(user.getId().toString(), adminPassword);
		Authentication auth = authManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

}
