package jp.ac.hosei.media.peas.repository;

import jp.ac.hosei.media.peas.domain.LTIConsumer;
import jp.ac.hosei.media.peas.domain.LTIContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LTIContextRepository extends JpaRepository<LTIContext, Long> {
	@Query("select u from LTIContext u where u.contextId = :contextId and u.ltiConsumer = :ltiConsumer")
    LTIContext findByContextId(@Param("contextId") String contextId, @Param("ltiConsumer") LTIConsumer ltiConsumer);
}
