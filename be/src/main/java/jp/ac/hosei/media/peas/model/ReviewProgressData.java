package jp.ac.hosei.media.peas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewProgressData {
	private int submitted;
	private int count;
}
