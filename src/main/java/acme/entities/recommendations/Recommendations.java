
package acme.entities.recommendations;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recommendations extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Optional
	@ValidString(min = 1, max = 50)
	@Automapped
	private String					amadeusId;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String					name;

	@Optional
	@Enumerated(EnumType.STRING)
	@Automapped
	private RecommendationCategory	category;

	@Optional
	@ValidUrl
	@Automapped
	private String					bookingLink;

	@Optional
	@ValidUrl
	@Automapped
	private String					pictures;

	@Optional
	@ValidNumber(min = 0, max = 5)
	@Automapped
	private Double					rating;

	@Optional
	@ValidNumber(min = -90, max = 90)
	@Automapped
	private Double					latitude;

	@Optional
	@ValidNumber(min = -180, max = 180)
	@Automapped
	private Double					longitude;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport					airport;
}
