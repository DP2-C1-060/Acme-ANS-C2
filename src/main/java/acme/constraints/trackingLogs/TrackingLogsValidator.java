
package acme.constraints.trackingLogs;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claim.Claim;
import acme.entities.trackinglogs.TrackingLogStatus;
import acme.entities.trackinglogs.TrackingLogs;
import acme.entities.trackinglogs.TrackingLogsRepository;

public class TrackingLogsValidator extends AbstractValidator<ValidTrackingLogs, TrackingLogs> {

	@Autowired
	private TrackingLogsRepository repository;


	@Override
	protected void initialise(final ValidTrackingLogs annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLogs trackingLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean correctStatus = false;

				TrackingLogStatus status = trackingLog.getIndicator();
				Double percentage = trackingLog.getResolutionPercentage();

				if (percentage < 100 && status == TrackingLogStatus.PENDING || percentage == 100 && (status == TrackingLogStatus.ACCEPTED || status == TrackingLogStatus.REJECTED))
					correctStatus = true;

				super.state(context, correctStatus, "pending", "acme.validation.trackingLog.indicator.message");
			}
			{
				Double percentage = trackingLog.getResolutionPercentage();
				String resolution = trackingLog.getResolution();

				boolean hasResolutionWhen100Percent = percentage < 100 || resolution != null && !resolution.isBlank();

				super.state(context, hasResolutionWhen100Percent, "resolution", "acme.validation.trackingLog.resolution.message");
			}
			{
				Double resolutionPercentage = trackingLog.getResolutionPercentage();
				Date lastUpdateMoment = trackingLog.getLastUpdateMoment();
				Claim claim = trackingLog.getClaim();
				List<TrackingLogs> previousTrackingLogs = this.repository.findByClaimIdAndDateBefore(claim.getId(), lastUpdateMoment);

				Optional<Double> maxPercentage = previousTrackingLogs.stream().map(TrackingLogs::getResolutionPercentage).max(Double::compareTo);

				boolean isPercentageGreaterThanPrevious = resolutionPercentage == 0 || resolutionPercentage > maxPercentage.orElse(0.0);

				super.state(context, isPercentageGreaterThanPrevious, "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
