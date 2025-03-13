
package acme.entities.currency;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CurrencyConfiguration extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Mandatory
	@ValidString(pattern = "[A-Z]{3}")
	@Automapped
	private String				currency;

	@Mandatory
	@Automapped
	@ValidString
	private String				acceptedCurrency;
}
