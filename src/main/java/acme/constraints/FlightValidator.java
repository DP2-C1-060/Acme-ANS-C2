
package acme.constraints;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.currency.CurrencyRepository;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightRepository;
import acme.entities.legs.Leg;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightRepository	repository;

	@Autowired
	private CurrencyRepository	currencyRepository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (flight == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean hasLegs;

				List<Leg> flightLegs = this.repository.findLegsByFlightId(flight.getId());

				hasLegs = flight.isDraftMode() ? true : !flightLegs.isEmpty();

				super.state(context, hasLegs, "*", "acme.validation.flight.no-legs.message");
			}
			{
				boolean publishedLegs;

				List<Leg> flightLegs = this.repository.findLegsByFlightId(flight.getId());

				publishedLegs = flight.isDraftMode() ? true : flightLegs.stream().allMatch(l -> !l.isDraftMode());

				super.state(context, publishedLegs, "*", "acme.validation.flight.unpublished-legs.message");
			}
			{
				boolean validCurrency = true;

				if (flight.getCost() != null) {
					String allowedCurrenciesString = this.currencyRepository.findAllAcceptedCurrency();
					List<String> allowedCurrencies = Arrays.asList(allowedCurrenciesString.split(","));
					validCurrency = allowedCurrencies.contains(flight.getCost().getCurrency());

					super.state(context, validCurrency, "cost", "acme.validation.flight.invalid-currency.message");
				}
			}
			{
				boolean validSelfTransfer = true;

				if (!flight.isDraftMode()) {

					List<Leg> flightLegs = this.repository.findLegsByFlightId(flight.getId());
					List<Leg> publishedLegs = flightLegs.stream().filter(l -> !l.isDraftMode()).collect(Collectors.toList());

					if (publishedLegs.size() == 1)
						validSelfTransfer = !flight.isSelfTransfer();
				}

				super.state(context, validSelfTransfer, "selfTransfer", "acme.validation.flight.invalid-selftransfer.message");
			}
		}
		result = !super.hasErrors(context);

		return result;
	}

}
