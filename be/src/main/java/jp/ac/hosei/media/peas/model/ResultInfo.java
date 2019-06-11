package jp.ac.hosei.media.peas.model;

import java.util.Date;

import jp.ac.hosei.media.peas.domain.Target;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResultInfo {
	public String title;
	public long targetId;
	public Date reviewStarted;
	
	public ResultInfo(Target target) {
		this(target.getQuiz().getTitle(), target.getId(), target.getReviewStarted());
	}	
	
}
