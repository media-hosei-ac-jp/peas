package jp.ac.hosei.media.peas.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of={"id"})
public class QuizItem {
	public enum QuizType {Score, Free}
	
    @Id
    @GeneratedValue
    private Long id;
    
//    @Column
//    private Date created;
//    
//    @Column
//    private Date modified;
    
    @Lob
    private String text;
    
    @Column
    private int maxScore;
    
    @Enumerated(EnumType.STRING)
    private QuizType type;
    
    @Transient
    private String typeStr;
    
    public void setTypeStr(String typeStr) {
    	this.typeStr = typeStr;
    	type = QuizType.valueOf(typeStr);
    }
    
    public String getTypeStr() {
    	return type.toString();
    }
    
    @JsonIgnore
    @ManyToOne(targetEntity=Quiz.class)
    private Quiz parent;

}
