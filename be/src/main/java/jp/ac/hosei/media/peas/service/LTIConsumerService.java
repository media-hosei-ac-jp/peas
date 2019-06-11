package jp.ac.hosei.media.peas.service;

import jp.ac.hosei.media.peas.domain.LTIConsumer;
import jp.ac.hosei.media.peas.repository.LTIConsumerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LTIConsumerService {
	@Autowired
	private LTIConsumerRepository ltiConsumerRepository;

    public LTIConsumer save(LTIConsumer u) {
    	return ltiConsumerRepository.save(u);
    }

	public LTIConsumer findByConsumerKey(String consumerKey) {
		return ltiConsumerRepository.findByConsumerKey(consumerKey);
	}
}
