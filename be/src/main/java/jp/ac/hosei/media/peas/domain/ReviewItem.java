package jp.ac.hosei.media.peas.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of={"id"})
public class ReviewItem {
    @Id
    @GeneratedValue
	private Long id;
    
//    @Column
//    private Date created;
//    
//    @Column
//	private Date modified;
    
    @Lob
	private String content;
    
    /**
     * 自由記述コメントの非表示機能
     */
    @Column
    private boolean hidden; 
    
    @Column
	private int score;
    
    @ManyToOne
	private QuizItem quizItem;
	
    @JsonIgnore
	@ManyToOne(targetEntity=Review.class)
	private Review parent;
}
