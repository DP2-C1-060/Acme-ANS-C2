
package acme.constraints.log;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claim.Claim;
import acme.entities.logs.Log;
import acme.entities.logs.LogRepository;
import acme.entities.logs.LogStatus;

public class LogValidator extends AbstractValidator<ValidLog, Log> {

	@Autowired
	private LogRepository repository;


	@Override
	protected void initialise(final ValidLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Log log, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (log == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean correctStatus = false;

				LogStatus status = log.getIndicator();
				Double percentage = log.getResolutionPercentage();

				if (percentage < 100 && status == LogStatus.PENDING || percentage == 100 && (status == LogStatus.ACCEPTED || status == LogStatus.REJECTED))
					correctStatus = true;

				super.state(context, correctStatus, "pending", "acme.validation.trackingLog.indicator.message");
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
				List<Log> previousLogs = this.repository.findByClaimIdAndDateBefore(claim.getId(), lastUpdateMoment);

				Optional<Double> maxPercentage = previousLogs.stream().map(Log::getResolutionPercentage).max(Double::compareTo);

				boolean isPercentageGreaterThanPrevious = resolutionPercentage == 0 || resolutionPercentage > maxPercentage.orElse(0.0);

				super.state(context, isPercentageGreaterThanPrevious, "resolutionPercentage", "acme.validation.log.resolutionPercentage.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
