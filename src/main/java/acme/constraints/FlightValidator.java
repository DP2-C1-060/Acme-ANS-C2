
package acme.constraints;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

				List<Leg> flightLegs = this.repository.findLegsByFlight(flight.getId());

				hasLegs = flight.isDraftMode() ? true : !flightLegs.isEmpty();

				super.state(context, hasLegs, "tag", "acme.validation.flight.no-associated-legs.message");
			}
			{
				boolean publishedLegs;

				List<Leg> flightLegs = this.repository.findDraftingLegsByFlight(flight.getId());

				publishedLegs = flight.isDraftMode() ? true : flightLegs.isEmpty();

				super.state(context, publishedLegs, "tag", "acme.validation.flight.unpublished-legs.message");
			}
			{
				boolean validCurrency;

				String currencies = this.currencyRepository.findAllAcceptedCurrency();

				Set<String> acceptedCurrencies = Arrays.stream(currencies.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toSet());

				validCurrency = flight.getCost() == null || acceptedCurrencies.contains(flight.getCost().getCurrency());

				super.state(context, validCurrency, "cost", "acme.validation.flight.unsupported-currency.message");
			}
		}
		result = !super.hasErrors(context);

		return result;
	}

}
