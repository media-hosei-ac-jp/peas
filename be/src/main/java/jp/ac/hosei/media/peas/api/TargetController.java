package jp.ac.hosei.media.peas.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.hosei.media.peas.domain.Quiz;
import jp.ac.hosei.media.peas.domain.Target;
import jp.ac.hosei.media.peas.domain.User;
import jp.ac.hosei.media.peas.model.ReviewProgressData;
import jp.ac.hosei.media.peas.service.QuizService;
import jp.ac.hosei.media.peas.service.TargetService;
import jp.ac.hosei.media.peas.ws.WSHandler;
import jp.ac.hosei.media.peas.ws.WSHandler.MessageType;

//TODO refactor
@RestController
@RequestMapping("/api/target")
public class TargetController {
	@Autowired
	private TargetService targetService;
	
	@Autowired
	private QuizService quizService;
		
	@Autowired
	private WSHandler wsHandler;

	@RequestMapping(method=RequestMethod.GET)
	public List<Target> getTargets(@AuthenticationPrincipal User user, @RequestParam(name="quizId") Long quizId) {
		if(quizId!=null) {
			return targetService.getTargetsByQuizId(quizId);
		}
		
		throw new IllegalStateException();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public Target getTarget(@AuthenticationPrincipal User user, @PathVariable long id) {
		Target t = targetService.getTargetById(id);
		if(!user.getLtiResourceLink().equals(t.getQuiz().getParent())) {
			throw new SecurityException();
		}
		return t;		
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/guest/{id}")
	public Target getTarget4Guest(@PathVariable long id) {
		Target t = targetService.getTargetById(id);
		if(!t.getQuiz().isGuestReview()) {
			throw new SecurityException();
		}
		return t;		
	}
	
	@PreAuthorize("hasRole('Instructor')") 
	@RequestMapping(method=RequestMethod.POST, value="/updateTargeted")
	public Target updateTargeted(@AuthenticationPrincipal User user, @RequestBody Target target) {
		Target res = targetService.updateTargeted(target);		
		wsHandler.sendMessage(user, MessageType.UpdateTarget);		
		return res;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="getAllProgressData")
	public Map<Long, ReviewProgressData> getAllProgtessDataByQuizId(@AuthenticationPrincipal User user, @RequestParam("quizId")Long quizId) {
		if(quizId==null) {
			throw new IllegalStateException();
		}
		List<Target> tl = targetService.getTargetsByQuizId(quizId);
		
		Map<Long, ReviewProgressData> rm = new HashMap<Long, ReviewProgressData>();
		tl.forEach(t->{
			ReviewProgressData data = targetService.getReviewProgressData(t);
			rm.put(t.getId(), data);
		});
		return rm;
	}
	
	/**
	 * 新規作成
	 * @param user
	 * @param target
	 * @return
	 */
	@PreAuthorize("hasRole('Instructor')") 
	@RequestMapping(method=RequestMethod.POST, value="create")
	public Target post(@AuthenticationPrincipal User user, @RequestBody Target target, @RequestParam("quizId")Long quizId) {
		Quiz q = quizService.getQuizById(quizId);
		if(!user.getLtiResourceLink().equals(q.getParent())) {
			throw new SecurityException();
		}
		target.setQuiz(q);
		return targetService.save(target);
	}
	
	
	/**
	 * 更新のみに利用
	 * @param user
	 * @param target
	 * @return
	 */
	@PreAuthorize("hasRole('Instructor')") 
	@RequestMapping(method=RequestMethod.POST)
	public Target save(@AuthenticationPrincipal User user, @RequestBody Target target) {
		return validateAndSave(user, target);
	}
	
	private Target validateAndSave(User user, Target target) {
		Target _target = targetService.findOne(target.getId());
		if(!user.getLtiResourceLink().equals(_target.getQuiz().getParent())) {
			throw new SecurityException();
		}
		target.setQuiz(_target.getQuiz());
		return targetService.save(target);
	}
	
	@PreAuthorize("hasRole('Instructor')") 
	@RequestMapping(value="saveAll", method=RequestMethod.POST)
	public List<Target> saveAll(@AuthenticationPrincipal User user, @RequestBody List<Target> targets) {
		List<Target> ret = new ArrayList<Target>();
		targets.forEach(t->{
			Target rt = validateAndSave(user, t);
			ret.add(rt);
		});
		
		return ret;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="getTargetedProgressData")
	public Map<Long, ReviewProgressData> getTargetedProgtessDataByQuizId(@AuthenticationPrincipal User user) {

		Target target = targetService.getTargetedTarget(user.getLtiResourceLink());
		
		Map<Long, ReviewProgressData> rm = new HashMap<Long, ReviewProgressData>();
		if(target != null) {
			ReviewProgressData data = targetService.getReviewProgressData(target);
			rm.put(target.getId(), data);
		}

		return rm;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="getTargetedTarget")
	public Target getTargetedTarget(@AuthenticationPrincipal User user) {
		Target t = targetService.getTargetedTarget(user.getLtiResourceLink());
		if(t!=null && t.getUsers().contains(user)) {
			return null;
		}
		return t;
	}
}
