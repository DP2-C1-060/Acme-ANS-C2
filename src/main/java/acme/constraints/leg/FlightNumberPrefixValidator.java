/*
 * ValidFlightNumberPrefixValidator.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.constraints.leg;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.legs.Leg;

@Validator
public class FlightNumberPrefixValidator extends AbstractValidator<ValidFlightNumberPrefix, Leg> {

	@Override
	protected void initialise(final ValidFlightNumberPrefix annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		if (leg == null)
			return true;
		if (leg.getFlightNumber() == null || leg.getFlightNumber().length() < 3)
			return true;
		if (leg.getAircraft() == null || leg.getAircraft().getAirline() == null)
			return true;

		String flightNumberPrefix = leg.getFlightNumber().substring(0, 3);
		String airlineIata = leg.getAircraft().getAirline().getIataCode();

		boolean valid = flightNumberPrefix.equals(airlineIata);
		if (!valid)
			super.state(context, false, "flightNumber", "{acme.validation.leg.prefix.message}");

		return !super.hasErrors(context);
	}
}
