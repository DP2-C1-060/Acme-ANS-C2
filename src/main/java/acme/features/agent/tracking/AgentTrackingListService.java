
package acme.features.agent.tracking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.tracking.Tracking;
import acme.realms.agent.Agent;

@GuiService
public class AgentTrackingListService extends AbstractGuiService<Agent, Tracking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AgentTrackingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);
		status = claim != null && (!claim.isDraftMode() || super.getRequest().getPrincipal().hasRealm(claim.getAgent()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Tracking> trackings;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		trackings = this.repository.findTrackingsByMasterId(masterId);

		super.getBuffer().addData(trackings);
	}

	@Override
	public void unbind(final Tracking tracking) {
		Dataset dataset;

		dataset = super.unbindObject(tracking, "step", "lastUpdateMoment", "resolutionPercentage");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Tracking> trackings) {
		int masterId;
		Claim claim;
		final boolean showCreate;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);
		showCreate = super.getRequest().getPrincipal().hasRealm(claim.getAgent());

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
