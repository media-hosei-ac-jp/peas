package jp.ac.hosei.media.peas.repository;

import jp.ac.hosei.media.peas.domain.LTIResourceLink;
import jp.ac.hosei.media.peas.domain.Quiz;
import jp.ac.hosei.media.peas.domain.QuizItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizItemRepository extends JpaRepository<QuizItem, Long>{

}
