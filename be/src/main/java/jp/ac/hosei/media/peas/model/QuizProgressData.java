package jp.ac.hosei.media.peas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizProgressData {
	private int started;
	private int count;
}
