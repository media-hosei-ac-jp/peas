package jp.ac.hosei.media.peas.api;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imsglobal.aspect.Lti;
import org.imsglobal.lti.launch.LtiUser;
import org.imsglobal.lti.launch.LtiVerificationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.hosei.media.peas.domain.LTIConsumer;
import jp.ac.hosei.media.peas.domain.LTIContext;
import jp.ac.hosei.media.peas.domain.LTIResourceLink;
import jp.ac.hosei.media.peas.domain.Quiz;
import jp.ac.hosei.media.peas.domain.Role;
import jp.ac.hosei.media.peas.domain.Target;
import jp.ac.hosei.media.peas.domain.User;
import jp.ac.hosei.media.peas.service.AuthService;
import jp.ac.hosei.media.peas.service.LTIConsumerService;
import jp.ac.hosei.media.peas.service.LTIContextService;
import jp.ac.hosei.media.peas.service.LTIResourceLinkService;
import jp.ac.hosei.media.peas.service.QuizService;
import jp.ac.hosei.media.peas.service.UserService;
import lombok.Setter;

@RestController
@RequestMapping(value="/lti")
@ConfigurationProperties(prefix="settings")
public class LTIController {
	private static final String 
	KEY_LIS_PERSON_NAME_FAMILY = "lis_person_name_family",
	KEY_LIS_PERSON_NAME_GIVEN = "lis_person_name_given",
	KEY_LIS_PERSON_CONTACT_EMAIL_PRIMARY = "lis_person_contact_email_primary",
	KEY_OAUTH_CONSUMER_KEY = "oauth_consumer_key",
	KEY_CONTEXT_ID = "context_id", //site
	KEY_RESOURCE_LINK_ID = "resource_link_id"; //tool

	@Autowired
	private UserService userService;	
	@Autowired
	private LTIConsumerService ltiConsumerService;
	@Autowired
	private LTIContextService ltiContextService;
	@Autowired
	private LTIResourceLinkService ltiResourceLinkService;

	@Setter
	private String frontendUrl;

	@Autowired
	private AuthService authService;

	@Autowired
	private QuizService quizService;

	@Lti
	@RequestMapping(value = "/launch", method = RequestMethod.POST)
	public String ltiEntry(HttpServletRequest request, HttpServletResponse response, LtiVerificationResult result) {
		if(!result.getSuccess()){
			throw new SecurityException("lti launch error: "+result.getMessage());
		} else {
			//lti consumer validation あらかじめ登録したものに限る
			String consumerKey = request.getParameter(KEY_OAUTH_CONSUMER_KEY) ;
			LTIConsumer ltiConsumer = ltiConsumerService.findByConsumerKey(consumerKey);

			if(ltiConsumer == null) {
				throw new IllegalStateException("ltiConsumer is not found");
			}	    	
			String contextId = request.getParameter(KEY_CONTEXT_ID) ;
			LTIContext ltiContext = ltiContextService.findByContextId(contextId, ltiConsumer);
			if(ltiContext == null) {
				ltiContext = new LTIContext(null, contextId, ltiConsumer); 
				ltiContextService.save(ltiContext);
			}

			String resourceLinkId = request.getParameter(KEY_RESOURCE_LINK_ID) ;
			LTIResourceLink ltiResourceLink = ltiResourceLinkService.findByResourceLinkId(resourceLinkId, ltiContext);
			if(ltiResourceLink == null) {
				ltiResourceLink = new LTIResourceLink(null, resourceLinkId, ltiContext); 
				ltiResourceLinkService.save(ltiResourceLink);
			}
			//end validation

			//lti user
			LtiUser ltiUser = result.getLtiLaunchResult().getUser();
			String familyName = request.getParameter(KEY_LIS_PERSON_NAME_FAMILY);
			String givenName = request.getParameter(KEY_LIS_PERSON_NAME_GIVEN);
			String mail = request.getParameter(KEY_LIS_PERSON_CONTACT_EMAIL_PRIMARY);
			Set<String> roles = new TreeSet<>(ltiUser.getRoles());

			User user = userService.findByUserId(ltiUser.getId(), ltiResourceLink);
			if(user == null) { //新規登録
				Date created = new Date();
				user = new User(ltiUser.getId(), givenName, familyName, mail, roles, ltiResourceLink, created, created);
				user = userService.save(user);
				addNewTargets(ltiResourceLink, user);
			}
			else {
				Date lastLogin = new Date();
				user.setFamilyName(familyName);
				user.setGivenName(givenName);
				user.setMail(mail);
				// user.setRoles(roles); //とりあえずroleはアップデートしない（Provider側でのrole編集のため）	    		
				user.setLastLogin(lastLogin);
				user = userService.save(user);
			}

			// 認証（RoleをSecurityContextのAuthoritiesに追加）
			authService.authUser(user);

			try {
				response.sendRedirect(frontendUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return "success";
		}
	}

	private void addNewTargets(LTIResourceLink ltiResourceLink, User user) {
		List<Quiz> ql = quizService.getQuizzes(ltiResourceLink);
		for(Quiz q : ql) {

			Target t = new Target();
			t.setQuiz(q);
			t.getUsers().add(user);
			
			if(q.isAutoAddMember()) {
				t.setActive(user.hasRole(Role.Learner));
			}
			else {
				t.setActive(false);
			}
			
			q.getTargets().add(t);

			quizService.save(q);
		}
	}	

}
