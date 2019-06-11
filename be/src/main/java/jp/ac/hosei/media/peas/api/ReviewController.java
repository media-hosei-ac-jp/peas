package jp.ac.hosei.media.peas.api;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.hosei.media.peas.domain.Review;
import jp.ac.hosei.media.peas.domain.ReviewItem;
import jp.ac.hosei.media.peas.domain.Target;
import jp.ac.hosei.media.peas.domain.User;
import jp.ac.hosei.media.peas.service.ReviewService;
import jp.ac.hosei.media.peas.service.TargetService;
import jp.ac.hosei.media.peas.service.UserService;
import jp.ac.hosei.media.peas.ws.WSHandler;
import jp.ac.hosei.media.peas.ws.WSHandler.MessageType;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
	@Autowired 
	private ReviewService reviewService;
	
	@Autowired
	private TargetService targetService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WSHandler wsHandler;
	
	@RequestMapping(method=RequestMethod.GET, value="/getByTargetId/{targetId}")
	public Review getByTargetId(@AuthenticationPrincipal User user, @PathVariable long targetId) {
		return reviewService.get(user.getId(), targetId);
	}

	@RequestMapping(method=RequestMethod.POST, value="/{targetId}")
	public Review save(@AuthenticationPrincipal User user, @PathVariable long targetId, @RequestBody Review review) {
		review.setOwner(user);
		Target target = targetService.getTargetById(targetId);
		review.setTarget(target);
		review.setQuiz(target.getQuiz());
		
		for(ReviewItem item : review.getItems()) {
			item.setParent(review);
		}
		Review res = reviewService.save(review);
		wsHandler.sendMessage(user, MessageType.ReviewSubmitted);
		return res;
	}
	
	/**
	 * ゲスト用API
	 * @param targetId
	 * @param review
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/guest/{targetId}")
	public Review save4Guest(@PathVariable long targetId, @RequestBody Review review) {
		review.setOwner(null);
		Target target = targetService.getTargetById(targetId);
		if(!target.getQuiz().isGuestReview()) {
			throw new SecurityException();
		}
		review.setTarget(target);
		review.setQuiz(target.getQuiz());
		
		for(ReviewItem item : review.getItems()) {
			item.setParent(review);
		}
		Review res = reviewService.save(review);
		
		//wsでの通知用のダミーユーザを作成
		User user = new User();
		user.setLtiResourceLink(target.getQuiz().getParent());
		
		wsHandler.sendMessage(user, MessageType.ReviewSubmitted);
		return res;
	}
	
	@PreAuthorize("hasRole('Instructor')") 
	@RequestMapping(method=RequestMethod.GET, value="/setReviewItemHidden/{id}")
	public Map<String, Boolean> setReviewItemHidden(@AuthenticationPrincipal User user, @PathVariable long id, @PathParam(value = "value") boolean value) {
		reviewService.setReviewItemHiddenById(id, value);
		Map<String, Boolean> res = new HashMap<String, Boolean>();
		res.put("result", true);
		return res;
	}
	
	/**
	 * 投稿されたReviewを保存する。すでにあるものは上書きする。
	 * @param user
	 * @param ownerId
	 * @param targetId
	 * @param review
	 * @return
	 */
	@PreAuthorize("hasRole('Instructor')") 
	@RequestMapping(method=RequestMethod.POST, value="/{ownerId}/{targetId}")
	public Review save(@AuthenticationPrincipal User user, @PathVariable long ownerId, @PathVariable long targetId, @RequestBody Review review) {
		
		Target target = targetService.getTargetById(targetId);
		if(!user.getLtiResourceLink().equals(target.getQuiz().getParent())) {
			throw new SecurityException();
		}
		
		User owner = userService.findOne(ownerId);
		if(owner == null) {
			throw new IllegalArgumentException();
		}

		Review tr = reviewService.get(ownerId, targetId);
		if(tr!=null) {
			review.setId(tr.getId());
		}
		
		review.setOwner(owner);
		review.setTarget(target);
		review.setQuiz(target.getQuiz());
		
		for(ReviewItem item : review.getItems()) {
			item.setParent(review);
		}
		Review res = reviewService.save(review);
		wsHandler.sendMessage(user, MessageType.ReviewSubmitted);
		return res;
	}
}
