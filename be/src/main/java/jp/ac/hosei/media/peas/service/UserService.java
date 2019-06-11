package jp.ac.hosei.media.peas.service;

import java.util.List;

import jp.ac.hosei.media.peas.domain.LTIResourceLink;
import jp.ac.hosei.media.peas.domain.Target;
import jp.ac.hosei.media.peas.domain.User;
import jp.ac.hosei.media.peas.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public User findOne(Long id) {
		return userRepository.findOne(id);
	}

    public User save(User u) {
    	return userRepository.save(u);
    }

	public User findByMail(String mail) {
		return userRepository.findByMail(mail);
	}
	public User findByUserId(String userId, LTIResourceLink ltiResourceLink) {
		return userRepository.findByUserId(userId, ltiResourceLink);
	}

	public int countUsersByResourceLink(
			LTIResourceLink ltiResourceLink) {
		return userRepository.countUsersByResourceLink(ltiResourceLink);
	}

	public List<User> getLearnersByResourceLink(LTIResourceLink ltiResourceLink) {
		return userRepository.getLearnersByResourceLink(ltiResourceLink);
	}

	public List<User> getUsersByResourceLinkId(Long lrlId) {
		return userRepository.getUsersByResourceLinkId(lrlId);
	}
}
