package jp.ac.hosei.media.peas.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.hosei.media.peas.domain.Review;
import jp.ac.hosei.media.peas.domain.ReviewItem;
import jp.ac.hosei.media.peas.repository.ReviewRepository;


@Service
@Transactional
public class ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;

	public Review save(Review review) {
		Date now = new Date();
		if(review.getCreated()==null) {
			review.setCreated(now);
		}
		else {
			review.setModified(now);
		}
		return reviewRepository.save(review);
	}

	public Review get(long userId, long targetId) {
		return reviewRepository.find(userId, targetId);
	}

	public void setReviewItemHiddenById(long id, boolean value) {
		reviewRepository.setReviewItemHiddenById(id, value);
	}

	public void delete(Review review) {
		reviewRepository.delete(review);
	}

}
