package jp.ac.hosei.media.peas.api;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.hosei.media.peas.domain.User;
import jp.ac.hosei.media.peas.service.AuthService;
import jp.ac.hosei.media.peas.service.UserService;

/**
 * Admin Restコントローラー
 *
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserService userService;

	@PreAuthorize("hasRole('Administrator')")
	@RequestMapping(value="/login4A", method = RequestMethod.GET)
	public Map<String, Boolean> login4A(@RequestParam long uid) {
		boolean successful = authService.adminLogin(uid);		
		Map<String, Boolean> map = new TreeMap<>();
		map.put("successful", successful);
		return map;
	}
	
	@PreAuthorize("hasRole('Instructor')")
	@RequestMapping(value="/login4I", method = RequestMethod.GET)
	public Map<String, Boolean> login4I(@AuthenticationPrincipal User user, @RequestParam long uid) {
		User tu = userService.findOne(uid);
		boolean successful = false;
		
		if(tu!=null&&tu.getLtiResourceLink().equals(user.getLtiResourceLink())) {
			successful = authService.adminLogin(uid);			
		}
		
		Map<String, Boolean> map = new TreeMap<>();
		map.put("successful", successful);
		return map;
	}

}
