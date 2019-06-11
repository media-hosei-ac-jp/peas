package jp.ac.hosei.media.peas.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
public class Quiz {
	
    @Id
    @GeneratedValue
    private Long id;
    
    @Column
    private Date created;
    
    @Column
    private Date modified;
    
    @Column(nullable=false)
    private boolean resultPublished = true;
    
    @Column(nullable=false)
    private boolean deleted = false;
    
    @Column(nullable=false)
    private boolean anonymous;
    
    @Column(nullable=false)
    private String title;
    
    @Column
    private String description;
    
    /**
     * ログインなしで提出が可能か
     */
    @Column(nullable=false)
    private boolean guestReview = false;
    
    @OrderColumn(name="quizitem_index")
    @OneToMany(targetEntity=QuizItem.class, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        	name="quiz_quizitems",
        	joinColumns=@JoinColumn(name="quiz_id"),
        	inverseJoinColumns=@JoinColumn(name="quizitem_id")
        )  
    private List<QuizItem> items;
    
    @JsonIgnore
    @OneToMany(targetEntity=Target.class,  cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        	name="quiz_targets",
        	joinColumns=@JoinColumn(name="quiz_id"),
        	inverseJoinColumns=@JoinColumn(name="target_id")
        )  
    private List<Target> targets = new ArrayList<Target>();
    
    @JsonIgnore
    @ManyToOne
    private LTIResourceLink parent;
    
    /**
     * Learnerロールを自動で評価対象に追加するか
     */
    @Column
    private boolean autoAddMember = true;
}
