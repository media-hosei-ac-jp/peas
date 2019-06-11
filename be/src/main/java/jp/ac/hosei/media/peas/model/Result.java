package jp.ac.hosei.media.peas.model;

import java.util.ArrayList;
import java.util.List;

import jp.ac.hosei.media.peas.domain.Target;

public class Result {
	public String title;
	public String displayName;
	
	public List<ResultItem> items = new ArrayList<ResultItem>();	
	
	public Result(Target target) {
		this.title = target.getQuiz().getTitle();
		this.displayName = target.getDisplayName();
	}
}
