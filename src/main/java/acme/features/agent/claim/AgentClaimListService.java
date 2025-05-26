
package acme.features.agent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.realms.agent.Agent;

@GuiService
public class AgentClaimListService extends AbstractGuiService<Agent, Claim> {

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
		Collection<Claim> claims;
		int agentId;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findClaimsByAgentId(agentId);

		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "type", "registrationMoment", "leg.flightNumber");
		dataset.put("indicator", claim.getIndicator());

		super.getResponse().addData(dataset);
	}

}
