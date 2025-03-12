
package acme.constraints.leg;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;

@Validator
class ValidLegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Autowired
	private LegRepository legRepository;


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {

		assert context != null;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			if (leg.getDepartureAirport() != null && leg.getArrivalAirport() != null) {
				boolean distinctAirports = !leg.getDepartureAirport().equals(leg.getArrivalAirport());
				super.state(context, distinctAirports, "arrivalAirport", "acme.validation.leg.airports.message");
			}

			if (leg.getFlightNumber() != null && leg.getFlightNumber().length() >= 3 && leg.getAircraft() != null && leg.getAircraft().getAirline() != null) {
				String flightNumberPrefix = leg.getFlightNumber().substring(0, 3);
				String airlineIata = leg.getAircraft().getAirline().getIataCode();
				boolean validPrefix = flightNumberPrefix.equals(airlineIata);
				super.state(context, validPrefix, "flightNumber", "acme.validation.leg.prefix.message");
			}

			if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
				boolean validDates = leg.getScheduledArrival().after(leg.getScheduledDeparture());
				super.state(context, validDates, "scheduledArrival", "acme.validation.leg.dates.message");
			}

			if (leg.getFlightNumber() != null && leg.getFlightNumber().length() >= 7) {
				String flightNumber = leg.getFlightNumber();
				String lastFourDigits = flightNumber.substring(3);
				Leg existingLeg = this.legRepository.findLegByLastFourDigits(lastFourDigits);
				boolean unique = existingLeg == null || existingLeg.equals(leg);
				super.state(context, unique, "flightNumber", "acme.validation.leg.number.message");
			}
		}
		return !super.hasErrors(context);
	}
}
