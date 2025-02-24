
package acme.entities.legs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Airport extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Size(max = 3)
	@Automapped
	@Mandatory
	@Column(length = 3)
	private String				code;

	@NotBlank
	@Automapped
	@Mandatory
	private String				name;

	@NotBlank
	@Automapped
	@Mandatory
	private String				city;
}
