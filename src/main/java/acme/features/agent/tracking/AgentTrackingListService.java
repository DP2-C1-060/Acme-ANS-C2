
package acme.features.agent.tracking;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

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
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);
		status = claim != null && (!claim.isDraftMode() || super.getRequest().getPrincipal().hasRealm(claim.getAgent()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Tracking> trackings;
		int claimId;

		claimId = super.getRequest().getData("claimId", int.class);
		trackings = this.repository.findTrackingsByClaimId(claimId);

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
		int claimId;
		Claim claim;
		final boolean showCreate;

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);

		Collection<Double> topTwoPercentages = trackings.stream().map(Tracking::getResolutionPercentage).sorted(Comparator.reverseOrder()).limit(2).collect(Collectors.toList());

		boolean topTwoAreNotBoth100 = topTwoPercentages.stream().filter(p -> p == 100.0).count() < 2;

		showCreate = super.getRequest().getPrincipal().hasRealm(claim.getAgent()) && topTwoAreNotBoth100;

		super.getResponse().addGlobal("claimId", claimId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
