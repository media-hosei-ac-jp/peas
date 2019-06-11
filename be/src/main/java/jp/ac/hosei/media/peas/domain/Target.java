package jp.ac.hosei.media.peas.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

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
public class Target {

    @Id
    @GeneratedValue
    private long id;
    
    @JsonIgnore
    @ManyToOne
    private Quiz quiz;
    
    @ManyToMany(targetEntity=User.class)
    @JoinTable(
        	name="targets_users",
        	joinColumns=@JoinColumn(name="target_id"),
        	inverseJoinColumns=@JoinColumn(name="user_id")
        )  
    private List<User> users = new ArrayList<User>();   
    
    @Column
    private String groupName; //groupの場合に名前を設定する。nullの場合はListの0番目の名前
    
    public String getDisplayName() {
    	if(groupName!=null) {
    		return groupName;
    	}
    	User u = users.get(0);
    	return u.getFamilyName()+" "+u.getGivenName();
    }
    
    @Column
    private Date reviewStarted;
    
    @Column
    private Date reviewFinished;
    
    @Column
    private boolean resultPublished = false;
    
    /**
     * 初めて結果ページを開いた時間，未チェックはnull
     */
    @Column
    private Date checked;
    
    /**
     * 評価対象かどうか
     */
    @Column
    private boolean active;
    
    /**
     * 現在の評価対象として選ばれているか 
     */
    @Column
    private boolean targeted;
}
