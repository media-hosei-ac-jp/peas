package jp.ac.hosei.media.peas.service;

import jp.ac.hosei.media.peas.domain.LTIConsumer;
import jp.ac.hosei.media.peas.domain.LTIContext;
import jp.ac.hosei.media.peas.repository.LTIContextRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LTIContextService {
	@Autowired
	private LTIContextRepository ltiContextRepository;

    public LTIContext save(LTIContext u) {
    	return ltiContextRepository.save(u);
    }

	public LTIContext findByContextId(String contextId, LTIConsumer ltiConsumer) {
		return ltiContextRepository.findByContextId(contextId, ltiConsumer);
	}
}
