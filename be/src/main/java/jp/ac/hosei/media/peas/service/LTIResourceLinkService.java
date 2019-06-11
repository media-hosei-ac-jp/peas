package jp.ac.hosei.media.peas.service;

import jp.ac.hosei.media.peas.domain.LTIContext;
import jp.ac.hosei.media.peas.domain.LTIResourceLink;
import jp.ac.hosei.media.peas.repository.LTIResourceLinkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LTIResourceLinkService {
	@Autowired
	private LTIResourceLinkRepository ltiResourceLinkRepository;

    public LTIResourceLink save(LTIResourceLink u) {
    	return ltiResourceLinkRepository.save(u);
    }

	public LTIResourceLink findByResourceLinkId(String resourceLinkId, LTIContext ltiContext) {
		return ltiResourceLinkRepository.findByResourceLinkId(resourceLinkId, ltiContext);
	}
}
