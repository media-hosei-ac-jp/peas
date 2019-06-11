package jp.ac.hosei.media.peas.model;

import java.util.ArrayList;
import java.util.List;

import jp.ac.hosei.media.peas.domain.QuizItem;

public class ResultItem {
	public QuizItem quizItem;
	public int averageScore;
	public List<Comment> comments;

	public ResultItem(QuizItem quizItem) {
		this.quizItem = quizItem;
	}

}
