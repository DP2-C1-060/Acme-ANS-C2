
package acme.entities.claim;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLongText;
import acme.constraints.claim.ValidRegistrationMoment;
import acme.entities.legs.Leg;
import acme.entities.tracking.Tracking;
import acme.entities.tracking.TrackingRepository;
import acme.entities.tracking.TrackingStatus;
import acme.realms.agent.Agent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidRegistrationMoment
@Table(indexes = {
	@Index(columnList = "agent_id")
})
public class Claim extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				email;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public TrackingStatus getIndicator() {
		List<Tracking> results;
		TrackingRepository trackingRepository;

		trackingRepository = SpringHelper.getBean(TrackingRepository.class);
		results = trackingRepository.findLastTrackingsByClaimId(this.getId());

		return results.isEmpty() ? TrackingStatus.PENDING : results.get(0).getIndicator();
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Agent	agent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg		leg;

}
