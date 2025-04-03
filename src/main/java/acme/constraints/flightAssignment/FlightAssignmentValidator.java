
package acme.constraints.flightAssignment;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.assignments.FlightAssignment;
import acme.features.flightCrewMember.flightAssignment.FlightAssignmentRepository;
import acme.realms.flightCrewMember.AvailabilityStatus;

@Validator
public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment flightAssignment, final ConstraintValidatorContext context) {
		if (flightAssignment == null)
			return false;

		boolean memberAvailable;
		memberAvailable = flightAssignment.getMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
		super.state(context, memberAvailable, "member", "{acme.validation.FlightAssignment.memberNotAvailable.message}");

		boolean result = !super.hasErrors(context);
		return result;
	}

}
