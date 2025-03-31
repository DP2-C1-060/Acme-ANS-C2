
package acme.constraints.tracking;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claim.Claim;
import acme.entities.tracking.Tracking;
import acme.entities.tracking.TrackingRepository;
import acme.entities.tracking.TrackingStatus;

public class TrackingValidator extends AbstractValidator<ValidTracking, Tracking> {

	@Autowired
	private TrackingRepository repository;


	@Override
	protected void initialise(final ValidTracking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Tracking log, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (log == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean correctStatus = false;

				TrackingStatus status = log.getIndicator();
				Double percentage = log.getResolutionPercentage();

				if (percentage < 100 && status == TrackingStatus.PENDING || percentage == 100 && (status == TrackingStatus.ACCEPTED || status == TrackingStatus.REJECTED))
					correctStatus = true;

				super.state(context, correctStatus, "pending", "acme.validation.log.indicator.message");
			}
			{
				Double percentage = log.getResolutionPercentage();
				String resolution = log.getResolution();

				boolean hasResolutionWhen100Percent = percentage < 100 || resolution != null && !resolution.isBlank();

				super.state(context, hasResolutionWhen100Percent, "resolution", "acme.validation.log.resolution.message");
			}
			{
				Double resolutionPercentage = log.getResolutionPercentage();
				Date lastUpdateMoment = log.getLastUpdateMoment();
				Claim claim = log.getClaim();
				List<Tracking> previousTracking = this.repository.findByClaimIdAndDateBefore(claim.getId(), lastUpdateMoment);

				Optional<Double> maxPercentage = previousTracking.stream().map(Tracking::getResolutionPercentage).max(Double::compareTo);

				boolean isPercentageGreaterThanPrevious = resolutionPercentage == 0 || resolutionPercentage > maxPercentage.orElse(0.0);

				super.state(context, isPercentageGreaterThanPrevious, "resolutionPercentage", "acme.validation.log.resolutionPercentage.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
