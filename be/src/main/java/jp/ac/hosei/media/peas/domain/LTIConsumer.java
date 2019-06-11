package jp.ac.hosei.media.peas.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
public class LTIConsumer {
	@Id
    @GeneratedValue
    private Long id;
	
//	@Column(unique=true)
//	private String guid;
	
	@Column(unique=true)
	private String consumerKey;
	
	@JsonIgnore
	@Column
	private String secret;
}
