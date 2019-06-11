package jp.ac.hosei.media.peas.repository;

import jp.ac.hosei.media.peas.domain.LTIContext;
import jp.ac.hosei.media.peas.domain.LTIResourceLink;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LTIResourceLinkRepository extends JpaRepository<LTIResourceLink, Long> {
	@Query("select u from LTIResourceLink u where u.resourceLinkId = :resourceLinkId and u.ltiContext = :ltiContext")
    LTIResourceLink findByResourceLinkId(@Param("resourceLinkId") String ResourceLinkId, @Param("ltiContext") LTIContext ltiContext);
}
