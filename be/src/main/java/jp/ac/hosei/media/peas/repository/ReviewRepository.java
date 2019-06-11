package jp.ac.hosei.media.peas.repository;

import java.util.List;

import jp.ac.hosei.media.peas.domain.Quiz;
import jp.ac.hosei.media.peas.domain.QuizItem;
import jp.ac.hosei.media.peas.domain.Review;
import jp.ac.hosei.media.peas.domain.ReviewItem;
import jp.ac.hosei.media.peas.domain.Target;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long>{

	@Query("select r from Review r where r.owner.id = :userId and r.target.id = :targetId")
	Review find(@Param("userId")long userId, @Param("targetId")long targetId);

	@Query("select avg(ri.score)*100 from ReviewItem ri where ri.quizItem = :quizItem and ri.parent.target = :target")
	Integer getAverageScore(@Param("target") Target target, @Param("quizItem") QuizItem quizItem);
	
	@Query("select avg(ri.score)*100 from ReviewItem ri where ri.quizItem = :quizItem and ri.parent.target.active=true")
	Integer getAverageScoreByQuizItem(@Param("quizItem") QuizItem quizItem);


	@Query("select ri from ReviewItem ri where ri.quizItem = :quizItem and ri.parent.target = :target")
	List<ReviewItem> getAllReviewItems(@Param("target") Target target, @Param("quizItem") QuizItem quizItem);

	@Modifying
	@Query("update ReviewItem item set item.hidden = :value where item.id = :id")
	void setReviewItemHiddenById(@Param("id") long id, @Param("value") boolean value);
	
	@Query("select r from Review r where r.target = :target")
	List<Review> getAllReviews(@Param("target") Target target);

}
