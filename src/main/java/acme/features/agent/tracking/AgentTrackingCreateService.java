
package acme.features.agent.tracking;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.tracking.Tracking;
import acme.entities.tracking.TrackingStatus;
import acme.realms.agent.Agent;

@GuiService
public class AgentTrackingCreateService extends AbstractGuiService<Agent, Tracking> {

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
		status = claim != null && super.getRequest().getPrincipal().hasRealm(claim.getAgent());

		if (status) {
			Collection<Tracking> trackings = this.repository.findTrackingsByClaimId(claimId);
			Collection<Double> topTwoPercentages = trackings.stream().map(Tracking::getResolutionPercentage).sorted(Comparator.reverseOrder()).limit(2).collect(Collectors.toList());
			status = topTwoPercentages.stream().filter(p -> p == 100.0).count() < 2;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tracking tracking;
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);

		tracking = new Tracking();
		tracking.setDraftMode(true);
		tracking.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		tracking.setClaim(claim);

		super.getBuffer().addData(tracking);
	}

	@Override
	public void bind(final Tracking tracking) {
		super.bindObject(tracking, "resolution", "resolutionPercentage", "indicator", "step");
	}

	@Override
	public void validate(final Tracking tracking) {
		;
	}

	@Override
	public void perform(final Tracking tracking) {
		this.repository.save(tracking);
	}

	@Override
	public void unbind(final Tracking tracking) {
		Dataset dataset;
		SelectChoices stateChoices;

		stateChoices = SelectChoices.from(TrackingStatus.class, tracking.getIndicator());
		dataset = super.unbindObject(tracking, "resolution", "resolutionPercentage", "step", "indicator", "lastUpdateMoment", "draftMode");
		dataset.put("claimId", super.getRequest().getData("claimId", int.class));
		dataset.put("states", stateChoices);
		dataset.put("claimDraftMode", tracking.getClaim().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
