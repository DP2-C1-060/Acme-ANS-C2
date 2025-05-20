
package acme.features.agent.tracking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tracking.Tracking;
import acme.entities.tracking.TrackingStatus;
import acme.realms.agent.Agent;

@GuiService
public class AgentTrackingDeleteService extends AbstractGuiService<Agent, Tracking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AgentTrackingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trackingId;
		Tracking tracking;

		trackingId = super.getRequest().getData("id", int.class);
		tracking = this.repository.findTrackingById(trackingId);
		status = tracking != null && tracking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(tracking.getClaim().getAgent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tracking tracking;
		int id;

		id = super.getRequest().getData("id", int.class);
		tracking = this.repository.findTrackingById(id);

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
		this.repository.delete(tracking);
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
