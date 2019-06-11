package jp.ac.hosei.media.peas.repository;

import java.util.List;

import jp.ac.hosei.media.peas.domain.LTIResourceLink;
import jp.ac.hosei.media.peas.domain.Quiz;
import jp.ac.hosei.media.peas.domain.Target;
import jp.ac.hosei.media.peas.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("select u from User u where u.userId = :userId and u.ltiResourceLink = :ltiResourceLink")
    User findByUserId(@Param("userId") String uid, @Param("ltiResourceLink") LTIResourceLink ltiResourceLink);

	@Query("select u from User u where u.mail = :mail")
	User findByMail(@Param("mail") String mail);

	@Query("select u from User u where :role in u.roles")
	List<User> getAllByRole(@Param("role") String role);

	@Query("select count(u) from User u where u.ltiResourceLink = :ltiResourceLink")
	int countUsersByResourceLink(@Param("ltiResourceLink") LTIResourceLink ltiResourceLink);
	
	@Query("select u from User u where u.ltiResourceLink = :ltiResourceLink")
	List<User> getLearnersByResourceLink(@Param("ltiResourceLink") LTIResourceLink ltiResourceLink);

	@Query("select u from User u where u.ltiResourceLink.id = :lrlId")
	List<User> getUsersByResourceLinkId(@Param("lrlId") long lrlId);

}
