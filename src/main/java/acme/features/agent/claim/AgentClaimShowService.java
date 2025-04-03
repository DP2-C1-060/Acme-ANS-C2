/*
 * AgentClaimShowService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

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
import acme.realms.agent.Agent;

@GuiService
public class AgentClaimShowService extends AbstractGuiService<Agent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;
		Agent agent;

		masterId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(masterId);
		agent = claim == null ? null : claim.getAgent();
		status = super.getRequest().getPrincipal().hasRealm(agent) || claim != null && !claim.isDraftMode();

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
	public void unbind(final Claim claim) {
		int agentId;
		Collection<Leg> legs;
		SelectChoices legChoices;
		SelectChoices typeChoices;
		Dataset dataset;

		if (!claim.isDraftMode())
			legs = this.repository.findAllLegs();
		else {
			agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			legs = this.repository.findLegsByAgentId(agentId);
		}
		typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "description", "email", "type", "registrationMoment", "draftMode");

		dataset.put("types", typeChoices);

		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}

}
