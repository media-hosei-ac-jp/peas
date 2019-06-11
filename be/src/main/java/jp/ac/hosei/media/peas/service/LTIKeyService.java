package jp.ac.hosei.media.peas.service;

import jp.ac.hosei.media.peas.domain.LTIConsumer;

import org.imsglobal.aspect.LtiKeySecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LTIKeyService implements LtiKeySecretService {
	@Autowired
	private LTIConsumerService ltiConsumerService;
	
	@Override
    public String getSecretForKey(String key) {
        LTIConsumer ltiConsumer = ltiConsumerService.findByConsumerKey(key);
        if(ltiConsumer==null) {
        	throw new IllegalStateException("LTIConsumer is not found");
        }
        return ltiConsumer.getSecret();
    }
}
