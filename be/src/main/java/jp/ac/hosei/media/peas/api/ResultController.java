package jp.ac.hosei.media.peas.api;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.hosei.media.peas.domain.Role;
import jp.ac.hosei.media.peas.domain.Target;
import jp.ac.hosei.media.peas.domain.User;
import jp.ac.hosei.media.peas.model.Result;
import jp.ac.hosei.media.peas.model.ResultInfo;
import jp.ac.hosei.media.peas.service.TargetService;

@RestController
@RequestMapping("/api/result")
public class ResultController {
	@Autowired
	private TargetService targetService;
	
	@RequestMapping(method=RequestMethod.GET, value="/getAllResultInfo")
	public List<ResultInfo> getAllResultInfo(@AuthenticationPrincipal User user) {
		List<Target> tl = targetService.getTargetsByUser(user);
		return tl.stream().filter(t->t.getQuiz().isResultPublished()||t.isResultPublished()).map(t->new ResultInfo(t)).collect(Collectors.toList());
	}

	@RequestMapping(method=RequestMethod.GET, value="/{targetId}")
	public Result get(@AuthenticationPrincipal User user, @PathVariable long targetId) {
		Target t = targetService.getTargetById(targetId);
		if(!((hasAdminRole(user) && t.getQuiz().getParent().equals(user.getLtiResourceLink()))
				|| (t.getUsers().contains(user) && (t.getQuiz().isResultPublished() || t.isResultPublished())))){
			throw new SecurityException();
		}
		
		if(t.getUsers().contains(user)) {
			t.setChecked(new Date());
			targetService.save(t);
		}
		
		return targetService.getResult(t, hasAdminRole(user)); 
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/getAverageByTargetId/{targetId}")
	public Result getAverageByTargetId(@AuthenticationPrincipal User user, @PathVariable long targetId) {
		Target t = targetService.getTargetById(targetId);
		if(!((hasAdminRole(user) && t.getQuiz().getParent().equals(user.getLtiResourceLink()))
				|| (t.getUsers().contains(user) && (t.getQuiz().isResultPublished() || t.isResultPublished())))){
			throw new SecurityException();
		}
		return targetService.getAverageResult(t); 
	}
	
	private boolean hasAdminRole(User user) {
		return user.hasRole(Role.Instructor);
	}
}
