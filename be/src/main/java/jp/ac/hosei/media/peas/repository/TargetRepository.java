package jp.ac.hosei.media.peas.repository;

import java.util.List;

import jp.ac.hosei.media.peas.domain.LTIResourceLink;
import jp.ac.hosei.media.peas.domain.Quiz;
import jp.ac.hosei.media.peas.domain.Target;
import jp.ac.hosei.media.peas.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TargetRepository extends JpaRepository<Target, Long>{
	@Query("select t from Target t where t.quiz = :quiz")
	List<Target> findAll(@Param("quiz") Quiz quiz);

	@Query("select t from Target t where t.quiz.id = :quizId")
	List<Target> findAllByQuizId(@Param("quizId")long quizId);

	@Modifying
	@Query("update Target t set t.targeted = false where t.quiz.id in " //modifyingはサブクエリを使わないとエラーになるっぽい
			+ "(select q.id from Quiz q where q.parent = :ltiResourceLink)")
	void distargetAll(@Param("ltiResourceLink")LTIResourceLink ltiResourceLink);

	@Query("select count(r) from Review r where r.target = :target")
	int getSubmittedCount(@Param("target") Target t);

	@Query("select t from Target t where t.targeted = true and t.quiz.parent = :ltiResourceLink")
	Target getTargetedTarget(@Param("ltiResourceLink")LTIResourceLink ltiResourceLink);

	@Query("select t from Target t where t.reviewStarted is not null and :user member of t.users")
	List<Target> findStartedTargetsByUser(@Param("user") User user);

	@Query("select t from Target t where :user member of t.users")
	List<Target> findTargetsByUser(@Param("user") User user);
}
