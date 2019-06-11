package jp.ac.hosei.media.peas.ws;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jp.ac.hosei.media.peas.domain.User;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WSHandler extends TextWebSocketHandler {
	public enum MessageType { UpdateTarget, ReviewSubmitted };
	/**
	 * セッションプールです。
	 */
	private Map<String, WebSocketSession> sessionPool = new ConcurrentHashMap<>();
	/**
	 * 接続が確立したセッションをプールします。
	 * @param session セッション
	 * @throws Exception 例外が発生した場合
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		this.sessionPool.put(session.getId(), session);
	}
	/**
	 * 切断された接続をプールから削除します。
	 * @param session セッション
	 * @param status ステータス
	 * @throws Exception 例外が発生した場合
	 */
	@Override
	public void afterConnectionClosed(
			WebSocketSession session,
			CloseStatus status) throws Exception {
		this.sessionPool.remove(session.getId());
	}

	public void sendMessage(User sender, MessageType messageType) {
		Map<String, Object> val = new HashMap<>();
		val.put("msg", messageType.toString());
		
		Set<String> keysToBeRemoved = new HashSet<>();
		for (Entry<String, WebSocketSession> entry : this.sessionPool.entrySet()) {
			WebSocketSession session = entry.getValue();
			if(session.getPrincipal()==null) { //セッションのnullpo切れ対策
				keysToBeRemoved.add(session.getId());
				continue;
			}
			User receiver = (User)((Authentication)session.getPrincipal()).getPrincipal();
			
			if(!receiver.getLtiResourceLink().equals(sender.getLtiResourceLink())) {
				return;
			}
			TextMessage msg;
			try {
				msg = new TextMessage(new ObjectMapper().writeValueAsString(val), true);
				session.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}    		
		}  
	
		for(String key : keysToBeRemoved) {
			sessionPool.remove(key);
		}
	}

}
