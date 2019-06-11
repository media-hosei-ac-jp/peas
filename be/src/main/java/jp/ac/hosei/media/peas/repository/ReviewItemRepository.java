package jp.ac.hosei.media.peas.repository;

import jp.ac.hosei.media.peas.domain.Quiz;
import jp.ac.hosei.media.peas.domain.ReviewItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewItemRepository extends JpaRepository<ReviewItem, Long> {

}
