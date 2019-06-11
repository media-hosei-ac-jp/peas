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
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"context_id", "lti_consumer_id"})})
public class LTIContext {
	@Id
    @GeneratedValue
    private Long id;
	
	@Column(name="context_id")
	private String contextId;
	
	@ManyToOne(targetEntity=LTIConsumer.class)
	@JoinColumn(name="lti_consumer_id")
	private  LTIConsumer ltiConsumer;
}
