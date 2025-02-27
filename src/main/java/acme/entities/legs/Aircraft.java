
package acme.entities.legs;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Automapped
	@Mandatory
	private String				model;

	@Automapped
	@Mandatory
	private Integer				capacity;
}
