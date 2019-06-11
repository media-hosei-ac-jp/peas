package jp.ac.hosei.media.peas.model;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jp.ac.hosei.media.peas.domain.ReviewItem;
import jp.ac.hosei.media.peas.domain.Role;
import jp.ac.hosei.media.peas.domain.User;

public class Comment {
	public long reviewItemId;
	public String name;
	public Set<String> roles;
	public String content;
	public boolean hidden;
	
	public Comment(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public Comment(ReviewItem reviewItem, boolean anonymous) {
		this.reviewItemId = reviewItem.getId();
		User owner = reviewItem.getParent().getOwner();
		if(owner!=null) {
			this.name = anonymous?"":owner.getFamilyName() + " " + owner.getGivenName();
			this.roles = owner.getRoles();
		}
		else { //ゲストレビュー用
			this.name = "guest";
			this.roles = new TreeSet<>();
			this.roles.add(Role.Learner.toString());
		}
		this.content = reviewItem.getContent();
		this.hidden = reviewItem.isHidden();
	}
}
