package jp.ac.hosei.media.peas.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.hosei.media.peas.domain.LTIResourceLink;
import jp.ac.hosei.media.peas.domain.Quiz;
import jp.ac.hosei.media.peas.domain.Review;
import jp.ac.hosei.media.peas.domain.Target;
import jp.ac.hosei.media.peas.model.QuizProgressData;
import jp.ac.hosei.media.peas.repository.QuizRepository;

@Service
@Transactional
public class QuizService {
	@Autowired
	private QuizRepository quizRepository;
	
	@Autowired
	private TargetService targetService;	 
	
	public Quiz save(Quiz quiz) {
		Date now = new Date();
		if(quiz.getCreated()==null) {
			quiz.setCreated(now);
		}
		else {
			quiz.setModified(now);
		}
		return quizRepository.save(quiz);
	}

	public Quiz getQuizById(long id) {
		return quizRepository.findOne(id);
	}

	public List<Quiz> getQuizzes(LTIResourceLink ltiResourceLink) {
		return quizRepository.findQuizzes(ltiResourceLink);
	}

	public QuizProgressData getQuizProgressData(Quiz q) {
		int count = quizRepository.getActiveTargetsCount(q);
		int started = quizRepository.getStartedTargetsCount(q);		
		return new QuizProgressData(started, count);
	}

	public Quiz getQuizByTargetId(long id) {
		return quizRepository.findByTargetId(id);
	}

	public Quiz getTargettedQuizId(LTIResourceLink ltiResourceLink) {
		Target t = targetService.getTargetedTarget(ltiResourceLink);
		if(t==null) {
			return null;
		}
		return t.getQuiz();
	}

}
