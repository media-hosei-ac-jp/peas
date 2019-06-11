package jp.ac.hosei.media.peas.domain;

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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
//@Table(uniqueConstraints={@UniqueConstraint(columnNames={"owner_id", "target_id"})}) //ゲスト提出のため無効化　
public class Review {
    @Id
    @GeneratedValue
    private Long id;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="owner_id")
	private User owner;
	
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="target_id")
	private Target target;
    
    @OneToMany(targetEntity=ReviewItem.class, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        	name="review_reviewitems",
        	joinColumns=@JoinColumn(name="review_id"),
        	inverseJoinColumns=@JoinColumn(name="reviewitem_id")
        ) 
	private List<ReviewItem> items;
    
    @JsonIgnore
    @ManyToOne
	private Quiz quiz;
    
    @Column
    private Date created;
    
    @Column
    private Date modified;
	
}
