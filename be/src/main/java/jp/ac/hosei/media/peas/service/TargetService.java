package jp.ac.hosei.media.peas.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jp.ac.hosei.media.peas.domain.LTIResourceLink;
import jp.ac.hosei.media.peas.domain.Quiz;
import jp.ac.hosei.media.peas.domain.QuizItem;
import jp.ac.hosei.media.peas.domain.ReviewItem;
import jp.ac.hosei.media.peas.domain.Target;
import jp.ac.hosei.media.peas.domain.User;
import jp.ac.hosei.media.peas.domain.QuizItem.QuizType;
import jp.ac.hosei.media.peas.model.Comment;
import jp.ac.hosei.media.peas.model.Result;
import jp.ac.hosei.media.peas.model.ResultItem;
import jp.ac.hosei.media.peas.model.ReviewProgressData;
import jp.ac.hosei.media.peas.repository.QuizRepository;
import jp.ac.hosei.media.peas.repository.ReviewRepository;
import jp.ac.hosei.media.peas.repository.TargetRepository;
import jp.ac.hosei.media.peas.repository.UserRepository;
import jp.ac.hosei.media.peas.ws.WSHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;

@Service
@Transactional
public class TargetService {
	@Autowired
	private TargetRepository targetRepository;
	
	@Autowired
	private QuizRepository quizRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Target get(Quiz quiz, List<User> ul) {
		//parent=quizのList<Target>を引っ張ってきてulと一致するかをチェックする
		List<Target> tl = targetRepository.findAll(quiz);
		for(Target t : tl) {
			if(compareUserList(ul, t.getUsers())) {
				return t;
			}
		}
	
		return null;
	}

	private boolean compareUserList(List<User> l1, List<User> l2) {
		if(l1.size()!=l2.size()) {
			return false;
		}
		
		Comparator<User> luc = (u1, u2) -> {
			return u1.getId().compareTo(u2.getId());
		};
		Collections.sort(l1, luc);
		Collections.sort(l2, luc);
		for(int i=0; i<l1.size(); i++) {
			if(!l1.get(i).getId().equals(l2.get(i).getId())) {
				return false;
			}
		}
		return true;
	}

	public List<Target> getTargetsByQuizId(long quizId) {
		return targetRepository.findAllByQuizId(quizId);
	}

	public Target updateTargeted(Target target) {
		boolean targeted = target.isTargeted();
		target = targetRepository.findOne(target.getId());
		targetRepository.distargetAll(target.getQuiz().getParent());

		target.setTargeted(targeted);
		if(targeted&&target.getReviewStarted()==null) {
			target.setReviewStarted(new Date());
		}
		return targetRepository.save(target);
	}

	public ReviewProgressData getReviewProgressData(Target t) {
		int submitted = targetRepository.getSubmittedCount(t);
		//int count = quizRepository.getTargetsCount(t.getQuiz())-1;
		int count = userRepository.countUsersByResourceLink(t.getQuiz().getParent())-t.getUsers().size();
		return new ReviewProgressData(submitted, count);
	}

	public Target getTargetedTarget(LTIResourceLink ltiResourceLink) {
		return targetRepository.getTargetedTarget(ltiResourceLink);
	}

	public Target getTargetById(long id) {
		return targetRepository.findOne(id);
	}

	public List<Target> getStartedTargetsByUser(User user) {
		return targetRepository.findStartedTargetsByUser(user);
	}
	
	public List<Target> getTargetsByUser(User user) {
		return targetRepository.findTargetsByUser(user);
	}

	public Target save(Target t) {
		return targetRepository.save(t);
	}

	public Result getResult(Target target, boolean isAdmin) {
		Result res = new Result(target);
		
		if(reviewRepository.getAllReviews(target).isEmpty()) {
			return null;
		}
		
		for(QuizItem qi : target.getQuiz().getItems()) {
			ResultItem ri = new ResultItem(qi);
			switch(qi.getType()) {
			case Score:
				Integer average = reviewRepository.getAverageScore(target, qi);
				ri.averageScore = average==null?0:average;
				break;
			case Free:
				List<ReviewItem> rvi = reviewRepository.getAllReviewItems(target, qi);
				if(isAdmin) {
					ri.comments = rvi.stream().map(i->new Comment(i,false)).collect(Collectors.toList());
				}
				else {
					ri.comments = rvi.stream().filter(i->!i.isHidden()).map(i->new Comment(i,target.getQuiz().isAnonymous())).collect(Collectors.toList());
				}
				break;
			default:
				throw new IllegalStateException();
			}
			res.items.add(ri);
		}
		return res;
	}

	public Target findOne(long id) {
		return targetRepository.findOne(id);
	}

	public Result getAverageResult(Target target) {
		Result res = new Result(target);
		
		if(reviewRepository.getAllReviews(target).isEmpty()) {
			return null;
		}
		
		for(QuizItem qi : target.getQuiz().getItems()) {
			ResultItem ri = new ResultItem(qi);
			switch(qi.getType()) {
			case Score:
				Integer average = reviewRepository.getAverageScoreByQuizItem(qi);
				ri.averageScore = average==null?0:average;
				break;
			case Free:
				break;
			default:
				throw new IllegalStateException();
			}
			res.items.add(ri);
		}
		return res;
	}
}
