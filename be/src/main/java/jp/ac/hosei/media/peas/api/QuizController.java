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
import org.springframework.web.bind.annotation.RestController;

import jp.ac.hosei.media.peas.domain.Quiz;
import jp.ac.hosei.media.peas.domain.QuizItem;
import jp.ac.hosei.media.peas.domain.Role;
import jp.ac.hosei.media.peas.domain.Target;
import jp.ac.hosei.media.peas.domain.User;
import jp.ac.hosei.media.peas.model.QuizProgressData;
import jp.ac.hosei.media.peas.service.QuizService;
import jp.ac.hosei.media.peas.service.TargetService;
import jp.ac.hosei.media.peas.service.UserService;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {
	@Autowired
	private QuizService quizService;

	@Autowired
	private UserService userService;

	@Autowired
	private TargetService targetService;

	@PreAuthorize("hasRole('Instructor')") 
	@RequestMapping(method=RequestMethod.POST)
	public Quiz save(@AuthenticationPrincipal User user, @RequestBody Quiz quiz) {
		quiz.setParent(user.getLtiResourceLink());
		for(QuizItem item : quiz.getItems()) {
			item.setParent(quiz);
		}

		boolean newObj = false;
		if(quiz.getId()==null) {
			newObj = true;
		}
		quiz = quizService.save(quiz); 
		if(newObj && quiz.isAutoAddMember()) {
			addNewTargets(quiz);
		}
		return quiz;
	}

	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public Quiz getQuiz(@AuthenticationPrincipal User user, @PathVariable long id) {
		Quiz q = quizService.getQuizById(id);
		if(!q.isGuestReview() && //ゲスト提出不可でメンバーじゃない場合はエラー
				!user.getLtiResourceLink().equals(q.getParent())) {
			throw new SecurityException();
		}

		return q;
	}
	
	@PreAuthorize("hasRole('Instructor')") 
	@RequestMapping(method=RequestMethod.GET, value="/addNewTargetsById/{id}")
	public void addNewTargets(@AuthenticationPrincipal User user, @PathVariable long id) {
		Quiz q = quizService.getQuizById(id);
		List<User> ul = userService.getLearnersByResourceLink(q.getParent());
		boolean modified = false;
		for(User u : ul) {
			List<User> l = new ArrayList<User>();
			l.add(u);
			Target t = targetService.get(q, l);
			if(t==null) {
				modified = true;
				t = new Target();
				t.setQuiz(q);
				t.getUsers().add(u);
				t.setActive(u.hasRole(Role.Learner));
				q.getTargets().add(t);
			}
		};
		if(modified) {
			quizService.save(q);
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="/getByTargetId/{id}")
	public Quiz getQuizByTargetId(@AuthenticationPrincipal User user, @PathVariable long id) {
		Quiz q = quizService.getQuizByTargetId(id);
		if(!user.getLtiResourceLink().equals(q.getParent())) {
			throw new SecurityException();
		}

		return q;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/guest/getByTargetId/{id}")
	public Quiz getQuizByTargetId4Guest(@AuthenticationPrincipal User user, @PathVariable long id) {
		Quiz q = quizService.getQuizByTargetId(id);
		if(!q.isGuestReview()) {
			throw new SecurityException();
		}

		return q;
	}

	private void addNewTargets(Quiz q) {
		List<User> ul = userService.getLearnersByResourceLink(q.getParent());
		for(User u : ul) {
			Target t = new Target();
			t.setQuiz(q);
			t.getUsers().add(u);
			t.setActive(u.hasRole(Role.Learner));
			q.getTargets().add(t);

		}
		quizService.save(q);
	}

	@RequestMapping(method=RequestMethod.GET)
	public List<Quiz> getQuizzes(@AuthenticationPrincipal User user) {
		List<Quiz> ql = quizService.getQuizzes(user.getLtiResourceLink());
		return ql;
	}

	@RequestMapping(method=RequestMethod.GET, value="getAllProgressData")
	public Map<Long, QuizProgressData> getAllProgtessData(@AuthenticationPrincipal User user) {
		List<Quiz> ql = quizService.getQuizzes(user.getLtiResourceLink());
		Map<Long, QuizProgressData> rm = new HashMap<Long, QuizProgressData>();
		ql.stream().forEach(q->{
			QuizProgressData data = quizService.getQuizProgressData(q);
			rm.put(q.getId(), data);
		});
		return rm;
	}

	@RequestMapping(method=RequestMethod.GET, value="/getTargettedQuiz")
	public Quiz getTargettedQuizId(@AuthenticationPrincipal User user) {
		return quizService.getTargettedQuizId(user.getLtiResourceLink());
	}
}
