
package acme.features.agent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.legs.Leg;
import acme.entities.tracking.Tracking;
import acme.realms.agent.Agent;

@GuiService
public class AgentClaimDeleteService extends AbstractGuiService<Agent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		Claim claim;
		Agent agent;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		agent = claim == null ? null : claim.getAgent();
		status = claim != null && claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(agent);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "description", "email", "type");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
		;
	}

	@Override
	public void perform(final Claim claim) {
		Collection<Tracking> trackings;

		trackings = this.repository.findTrackingsByClaimId(claim.getId());
		this.repository.deleteAll(trackings);
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<Leg> legs;
		Dataset dataset;
		SelectChoices typeChoices;
		SelectChoices legChoices;

		typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		legs = this.repository.findLegsByAgentId(claim.getAgent().getId());
		legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "description", "email", "type", "registrationMoment", "draftMode");

		dataset.put("types", typeChoices);

		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}
}
