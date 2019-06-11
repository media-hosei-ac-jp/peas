package jp.ac.hosei.media.peas.repository;

import jp.ac.hosei.media.peas.domain.LTIConsumer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LTIConsumerRepository extends JpaRepository<LTIConsumer, Long> {
	@Query("select u from LTIConsumer u where u.consumerKey = :consumerKey")
    LTIConsumer findByConsumerKey(@Param("consumerKey") String consumerKey);
}
