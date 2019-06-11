package jp.ac.hosei.media.peas.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of={"id"})
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"resource_link_id", "lti_context_id"})})
public class LTIResourceLink {
	@Id
    @GeneratedValue
    private Long id;
	
	@Column(name="resource_link_id")
	private String resourceLinkId;
	
	@ManyToOne(targetEntity=LTIContext.class)
	@JoinColumn(name="lti_context_id")
	private LTIContext ltiContext;
}
