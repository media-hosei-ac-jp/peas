package jp.ac.hosei.media.peas.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.hosei.media.peas.domain.User;
import jp.ac.hosei.media.peas.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value="/me", method=RequestMethod.GET)
	public User getCurrentUser(@AuthenticationPrincipal User user) {
		return user;
	}    
	
	@PreAuthorize("hasRole('Instructor')") 
	@RequestMapping(method=RequestMethod.GET)
	//public List<User> getUsersByLTIResource(@AuthenticationPrincipal User user, @RequestParam(name="lrlId") Long lrlId) {
	public List<User> getUsersByLTIResourceLink(@AuthenticationPrincipal User user) {
		return userService.getUsersByResourceLinkId(user.getLtiResourceLink().getId());		
	}

}
