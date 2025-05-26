
package acme.constraints.tracking;

import java.util.Comparator;
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
			Double percentage = log.getResolutionPercentage();
			if (percentage != null) {
				{
					boolean correctStatus = false;

					TrackingStatus status = log.getIndicator();
					if (percentage > 100 || percentage < 100 && status == TrackingStatus.PENDING || percentage == 100 && (status == TrackingStatus.ACCEPTED || status == TrackingStatus.REJECTED))
						correctStatus = true;
					super.state(context, correctStatus, "resolutionPercentage", "acme.validation.log.indicator.message");
				}
				{
					String resolution = log.getResolution();

					boolean hasResolutionWhen100Percent = percentage != 100 || !resolution.isBlank();
					super.state(context, hasResolutionWhen100Percent, "resolution", "acme.validation.log.resolution.message");
				}
				Date lastUpdateMoment = log.getLastUpdateMoment();
				Claim claim = log.getClaim();
				{
					List<Tracking> previousTracking = this.repository.findByClaimIdAndDateBefore(claim.getId(), lastUpdateMoment);
					previousTracking.removeIf(t -> t.equals(log));

					List<Double> sortedPercentages = previousTracking.stream().map(Tracking::getResolutionPercentage).sorted(Comparator.reverseOrder()).toList();

					Optional<Double> maxPercentage = sortedPercentages.stream().findFirst();

					boolean isPercentageGreaterThanPrevious = percentage >= maxPercentage.orElse(0.0) || percentage < 0;

					super.state(context, isPercentageGreaterThanPrevious, "resolutionPercentage", "acme.validation.log.resolutionPercentage.message");
				}
				{
					List<Tracking> previousTracking = this.repository.findTrackingsByClaimId(claim.getId());
					previousTracking.removeIf(t -> t.equals(log));

					List<Tracking> sortedTracking = previousTracking.stream().sorted(Comparator.comparing(Tracking::getResolutionPercentage).reversed()).toList();

					List<Double> sortedPercentages = sortedTracking.stream().map(Tracking::getResolutionPercentage).toList();

					Optional<Double> secondMaxPercentage = sortedPercentages.stream().skip(1).findFirst();

					boolean isNotThird100 = !secondMaxPercentage.orElse(0.0).equals(100.0) || percentage != 100;

					super.state(context, isNotThird100, "resolutionPercentage", "acme.validation.log.resolutionPercentage.exceptional");
				}
				{
					List<Tracking> previousTracking = this.repository.findTrackingsByClaimId(claim.getId());
					previousTracking.removeIf(t -> t.equals(log));

					List<Tracking> sortedTracking = previousTracking.stream().sorted(Comparator.comparing(Tracking::getResolutionPercentage).reversed()).toList();

					if (sortedTracking.size() >= 2) {
						Tracking first = sortedTracking.get(0);
						Tracking second = sortedTracking.get(1);

						boolean firstIs100AndDiffIndicator = log.getResolutionPercentage() != 100 || first.getResolutionPercentage() != 100.0 || second.getResolutionPercentage() == 100.0 || first.getIndicator().equals(log.getIndicator());

						super.state(context, firstIs100AndDiffIndicator, "indicator", "acme.validation.log.indicator.hundred");
					}
				}
				{
					List<Tracking> followingTracking = this.repository.findByClaimIdAndDateAfter(claim.getId(), lastUpdateMoment);

					List<Double> sortedPercentages = followingTracking.stream().map(Tracking::getResolutionPercentage).sorted().toList();

					Optional<Double> minPercentage = sortedPercentages.stream().findFirst();

					boolean isPercentageLessThanFollowing = minPercentage.map(min -> percentage <= min).orElse(true);

					super.state(context, isPercentageLessThanFollowing, "resolutionPercentage", "acme.validation.log.resolutionPercentage.following");
				}
				{
					if (log.getLastUpdateMoment() != null) {
						boolean isMomentAfterRegistration = !log.getLastUpdateMoment().before(log.getClaim().getRegistrationMoment());
						super.state(context, isMomentAfterRegistration, "resolutionPercentage", "acme.validation.log.lastUpdateMoment.message");
					}
				}
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
