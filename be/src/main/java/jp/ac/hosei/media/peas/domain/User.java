package jp.ac.hosei.media.peas.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"id", "lti_resource_link_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of={"id"})
public class User implements UserDetails {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4452848289211478192L;

	@Id
    @GeneratedValue
    private Long id;
    
    @Column
    private String userId;
    
    @Column
    private String givenName;
    
    @Column
    private String familyName;
    
    @Column
    private String mail;
    
    @ElementCollection(fetch=FetchType.EAGER) //no proxy error 対策でEAGERを指定
//    @ElementCollection(targetClass=Role.class) 
    @JoinTable(name="user_roles",joinColumns=@JoinColumn(name="user_id"))
    private Set<String> roles; 
    
//    @ManyToOne(targetEntity=LTIConsumer.class)
//    @JoinColumn(name="lti_consumer_id")
//    private  LTIConsumer ltiConsumer;
    
    @ManyToOne(targetEntity=LTIResourceLink.class)
    @JoinColumn(name="lti_resource_link_id")
    private  LTIResourceLink ltiResourceLink;
    
//    @OneToOne(mappedBy="owner")
//    private RecrInfo recrInfo;
    
//	private Set<GrantedAuthority> authorities;
    @Transient
    @JsonIgnore
	private boolean accountNonExpired = true;
    @Transient
    @JsonIgnore
	private boolean accountNonLocked = true;
    @Transient
    @JsonIgnore
	private boolean credentialsNonExpired = true;
    @Transient
    @JsonIgnore
	private boolean enabled = true;
    @Transient
    @JsonIgnore
	private String password;
    
    @Column
    private Date created;
    @Column
    private Date lastLogin;

    @JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		System.out.println("authority"+roles.stream().map(r->r.toGrantedAuthority()).collect(Collectors.toList()));
		return roles.stream().map(r->new SimpleGrantedAuthority(r)).collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return userId;
	}
	
	@JsonIgnore
    @ManyToMany(mappedBy="users")
    private List<Target> targets = new ArrayList<Target>();

	public User(String userId, String givenName, String familyName,
			String mail, Set<String> roles, LTIResourceLink ltiResourceLink,
			Date created, Date lastLogin) {
		super();
		this.userId = userId;
		this.givenName = givenName;
		this.familyName = familyName;
		this.mail = mail;
		this.roles = roles;
		this.ltiResourceLink = ltiResourceLink;
		this.created = created;
		this.lastLogin = lastLogin;
	}

	public boolean hasRole(Role role) {
		return getAuthorities().contains(role.toGrantedAuthority());
	}
    	
//    /**
//     * Json用の追加フィールド
//     */
//    @Transient
//    private Map<String, Object> extra = new TreeMap<String, Object>();
}
