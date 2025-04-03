
package acme.features.agent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.legs.Leg;
import acme.realms.agent.Agent;

@GuiService
public class AgentClaimCreateService extends AbstractGuiService<Agent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim claim;
		Agent agent;

		agent = (Agent) super.getRequest().getPrincipal().getActiveRealm();

		claim = new Claim();
		claim.setDraftMode(true);
		claim.setAgent(agent);
		claim.setRegistrationMoment(MomentHelper.getCurrentMoment());
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
		this.repository.save(claim);
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
