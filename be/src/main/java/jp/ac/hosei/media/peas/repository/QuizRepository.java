package jp.ac.hosei.media.peas.repository;

import java.util.List;

import jp.ac.hosei.media.peas.domain.LTIResourceLink;
import jp.ac.hosei.media.peas.domain.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
	@Query("select q from Quiz q where q.parent = :ltiResourceLink")
	List<Quiz> findQuizzes(@Param("ltiResourceLink") LTIResourceLink ltiResourceLink);

	@Query("select count(t) from Target t where t.quiz = :quiz")
	int countTargets(@Param("quiz") Quiz quiz);
	
	@Query("select count(t) from Target t where t.quiz = :quiz and t.active=true")
	int getActiveTargetsCount(@Param("quiz") Quiz quiz);
	

	@Query("select count(t) from Target t where t.quiz = :quiz and t.reviewStarted is not null and t.active=true")
	int getStartedTargetsCount(@Param("quiz") Quiz quiz);

	@Query("select q from Quiz q where q.id in "
			+ "(select t.quiz.id from Target t where t.id = :targetId)")
	Quiz findByTargetId(@Param("targetId")Long targetId);
	
//	@Query("select q from Quiz q where q.parent = :ltiResourceLink")
//	Quiz getActiveQuiz(@Param("ltiResourceLink") LTIResourceLink ltiResourceLink);

}
