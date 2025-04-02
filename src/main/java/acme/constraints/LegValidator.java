
package acme.constraints;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.airline.Airline;
import acme.entities.airport.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		// Si leg es null, no podemos validar nada más.
		if (leg == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		// Extraemos variables locales para simplificar y evitar encadenamientos.
		final Date scheduledDeparture = leg.getScheduledDeparture();
		final Date scheduledArrival = leg.getScheduledArrival();
		final String flightNumber = leg.getFlightNumber();
		final Aircraft aircraft = leg.getAircraft();
		final Flight flight = leg.getFlight();
		final Airport departureAirport = leg.getDepartureAirport();
		final Airport arrivalAirport = leg.getArrivalAirport();

		//----------------------------------------------------------------------
		// PREPARACIÓN DE LISTAS (solo se rellenan si hace falta)
		//----------------------------------------------------------------------

		// 1) Legs publicados del vuelo actual
		List<Leg> publishedLegsSameFlight = List.of();
		if (flight != null)
			publishedLegsSameFlight = this.repository.findPublishedLegsByFlightId(flight.getId());

		// 2) Legs publicados de la misma aeronave
		List<Leg> publishedLegsSameAircraft = List.of();
		if (aircraft != null && aircraft.getRegistrationNumber() != null)
			publishedLegsSameAircraft = this.repository.findPublishedLegsByAircraft(aircraft.getRegistrationNumber());

		// 3) Legs con los mismos últimos 4 dígitos
		List<Leg> legsWithSameLast4 = List.of();
		if (flightNumber != null && flightNumber.length() >= 4) {
			final String last4 = flightNumber.substring(flightNumber.length() - 4);
			legsWithSameLast4 = this.repository.findLegsByLast4Digits(last4);
		}

		//----------------------------------------------------------------------
		// 1) Llegada posterior a la salida (si no son null)
		//----------------------------------------------------------------------
		boolean rightOrder = true;
		if (scheduledDeparture != null && scheduledArrival != null)
			rightOrder = scheduledArrival.after(scheduledDeparture);
		super.state(context, rightOrder, "scheduledArrival", "acme.validation.leg.wrong-date-order.message");

		//----------------------------------------------------------------------
		// 2) IATA code correcto en flightNumber (las tres primeras letras)
		//----------------------------------------------------------------------
		boolean rightFlightNumber = true;
		if (flightNumber != null && flightNumber.length() >= 3 && aircraft != null && aircraft.getAirline() != null) {

			final Airline airline = aircraft.getAirline();
			if (airline.getIATAcode() != null && airline.getIATAcode().length() >= 3) {
				final String iata = airline.getIATAcode();
				rightFlightNumber = flightNumber.substring(0, 3).equals(iata);
			}
		}
		super.state(context, rightFlightNumber, "flightNumber", "acme.validation.leg.wrong-iata.message");

		//----------------------------------------------------------------------
		// 3) Duración entre 1 y 1000 minutos
		//----------------------------------------------------------------------
		boolean rightDuration = true;
		if (scheduledDeparture != null && scheduledArrival != null) {
			long differenceMillis = scheduledArrival.getTime() - scheduledDeparture.getTime();
			long differenceMins = differenceMillis / (1000 * 60);
			rightDuration = differenceMins >= 1 && differenceMins <= 1000;
		}
		super.state(context, rightDuration, "scheduledArrival", "acme.validation.leg.wrong-duration.message");

		//----------------------------------------------------------------------
		// 4) No solapar en la misma aeronave (para legs publicados)
		//    - Filtramos el leg actual y comprobamos solapes
		//----------------------------------------------------------------------
		boolean overlapedAircraft = true;
		if (aircraft != null && scheduledDeparture != null && scheduledArrival != null)
			if (flightNumber != null) {
				// Filtramos el leg actual de la lista y comprobamos si se solapa
				final List<Leg> otherLegs = publishedLegsSameAircraft.stream().filter(l -> !Objects.equals(l.getFlightNumber(), flightNumber)).filter(l -> l.getScheduledDeparture() != null && l.getScheduledArrival() != null).collect(Collectors.toList());

				overlapedAircraft = otherLegs.stream().noneMatch(existing -> {
					final Date eDep = existing.getScheduledDeparture();
					final Date eArr = existing.getScheduledArrival();
					// Comprobamos si se solapa en las franjas:
					//  - [existing.dep ... existing.arr] con [leg.dep ... leg.arr]
					boolean solapan = eDep.compareTo(scheduledDeparture) <= 0 && eArr.compareTo(scheduledDeparture) >= 0 || eDep.compareTo(scheduledArrival) <= 0 && eArr.compareTo(scheduledArrival) >= 0
						|| eDep.compareTo(scheduledDeparture) >= 0 && eArr.compareTo(scheduledArrival) <= 0;

					return solapan;
				});
			}
		super.state(context, overlapedAircraft, "aircraft", "acme.validation.leg.overlaped-aircraft.message");

		//----------------------------------------------------------------------
		// 5) Aeropuertos distintos
		//----------------------------------------------------------------------
		boolean differentAirports = true;
		if (departureAirport != null && arrivalAirport != null)
			differentAirports = !departureAirport.equals(arrivalAirport);
		super.state(context, differentAirports, "departureAirport", "acme.validation.leg.same-airport.message");

		//----------------------------------------------------------------------
		// 6) Últimos 4 dígitos únicos
		//----------------------------------------------------------------------
		boolean last4DigitsUnique = true;
		if (flightNumber != null && flightNumber.length() >= 4) {
			// Filtramos este leg y comprobamos si hay otros con esos últimos 4
			final List<Leg> found = legsWithSameLast4.stream().filter(l -> !l.equals(leg)).collect(Collectors.toList());
			last4DigitsUnique = found.isEmpty();
		}
		super.state(context, last4DigitsUnique, "flightNumber", "acme.validation.leg.last4-digits-not-unique.message");

		//----------------------------------------------------------------------
		// 7) Coherencia de tiempos entre legs consecutivos (publicados)
		//----------------------------------------------------------------------
		boolean validTimeLinks = true;
		if (flight != null && scheduledDeparture != null && scheduledArrival != null) {
			if (!publishedLegsSameFlight.contains(leg))
				publishedLegsSameFlight.add(leg);
			publishedLegsSameFlight.sort(Comparator.comparing(Leg::getScheduledDeparture));

			final int index = publishedLegsSameFlight.indexOf(leg);
			if (index != -1) {
				if (index > 0) {
					final Leg previous = publishedLegsSameFlight.get(index - 1);
					Date prevArr = previous.getScheduledArrival();
					if (prevArr != null && !prevArr.before(scheduledDeparture))
						validTimeLinks = false;
				}
				if (validTimeLinks && index < publishedLegsSameFlight.size() - 1) {
					final Leg next = publishedLegsSameFlight.get(index + 1);
					Date nextDep = next.getScheduledDeparture();
					if (nextDep != null && !nextDep.after(scheduledArrival))
						validTimeLinks = false;
				}
			}
		}
		super.state(context, validTimeLinks, "scheduledDeparture", "acme.validation.leg.invalid-time-link.message");

		// 8) Aeropuerto de llegadas y salidas consecutivas (selfTransfer / no selfTransfer)
		//	    Se comprueba solo si el Leg actual NO está en draftMode
		if (!leg.isDraftMode())
			if (flight != null && scheduledDeparture != null) {
				final boolean selfTransfer = flight.isSelfTransfer();

				if (!publishedLegsSameFlight.contains(leg))
					publishedLegsSameFlight.add(leg);
				publishedLegsSameFlight.sort(Comparator.comparing(Leg::getScheduledDeparture));

				final int index = publishedLegsSameFlight.indexOf(leg);
				if (index != -1) {

					if (index > 0) {
						final Leg previous = publishedLegsSameFlight.get(index - 1);
						final Airport previousArr = previous.getArrivalAirport();
						if (previousArr != null && departureAirport != null)
							if (selfTransfer) {
								if (previousArr.equals(departureAirport))
									super.state(context, false, "departureAirport", "acme.validation.leg.invalid-airport-link.previous.selfTransfer.message");
							} else if (!previousArr.equals(departureAirport))
								super.state(context, false, "departureAirport", "acme.validation.leg.invalid-airport-link.previous.message");
					}

					if (index < publishedLegsSameFlight.size() - 1) {
						final Leg next = publishedLegsSameFlight.get(index + 1);
						final Airport nextDep = next.getDepartureAirport();
						if (arrivalAirport != null && nextDep != null)
							if (selfTransfer) {
								if (arrivalAirport.equals(nextDep))
									super.state(context, false, "arrivalAirport", "acme.validation.leg.invalid-airport-link.next.selfTransfer.message");
							} else if (!arrivalAirport.equals(nextDep))
								super.state(context, false, "arrivalAirport", "acme.validation.leg.invalid-airport-link.next.message");
					}
				}
			}

		//----------------------------------------------------------------------
		// 9) No se repitan aeropuertos en la cadena, salvo en legs consecutivos
		//    (se comprueba con los publicados)
		//----------------------------------------------------------------------
		boolean validUniqueAirports = true;
		if (flight != null && departureAirport != null && arrivalAirport != null && scheduledArrival != null && scheduledDeparture != null) {

			if (!publishedLegsSameFlight.contains(leg))
				publishedLegsSameFlight.add(leg);
			publishedLegsSameFlight.sort(Comparator.comparing(Leg::getScheduledDeparture));

			final int index = publishedLegsSameFlight.indexOf(leg);
			if (index != -1) {
				final Leg previous = index > 0 ? publishedLegsSameFlight.get(index - 1) : null;
				final Leg next = index < publishedLegsSameFlight.size() - 1 ? publishedLegsSameFlight.get(index + 1) : null;

				final List<Leg> relevantLegs = publishedLegsSameFlight.stream().filter(other -> !other.equals(leg) && !Objects.equals(other, previous) && !Objects.equals(other, next)).collect(Collectors.toList());

				for (Leg other : relevantLegs) {
					final Airport otherDep = other.getDepartureAirport();
					final Airport otherArr = other.getArrivalAirport();

					// Comprobamos si se repite el dep o arr con los de "other"
					boolean repeated = departureAirport.equals(otherDep) || departureAirport.equals(otherArr) || arrivalAirport.equals(otherDep) || arrivalAirport.equals(otherArr);
					if (repeated) {
						validUniqueAirports = false;
						break;
					}
				}
			}
		}
		super.state(context, validUniqueAirports, "departureAirport", "acme.validation.leg.repeated-airport.message");

		//----------------------------------------------------------------------
		// 10) El avión debe estar activo (independientemente de su draftMode)
		//----------------------------------------------------------------------
		boolean aircraftActive = true;
		if (aircraft != null && aircraft.getStatus() != null)
			aircraftActive = aircraft.getStatus().equals(AircraftStatus.ACTIVE);
		super.state(context, aircraftActive, "aircraft", "acme.validation.leg.inactive-aircraft.message");

		return !super.hasErrors(context);
	}

}
