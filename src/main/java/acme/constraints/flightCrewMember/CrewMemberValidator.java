
package acme.constraints.flightCrewMember;

import java.util.Objects;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.realms.flightCrewMember.FlightCrewMember;
import acme.realms.flightCrewMember.FlightCrewMemberRepository;

public class CrewMemberValidator extends AbstractValidator<ValidCrewMember, FlightCrewMember> {

	@Autowired
	private FlightCrewMemberRepository repository;


	@Override
	protected void initialise(final ValidCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember member, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (member == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueAgent;
				FlightCrewMember existingAgent;

				existingAgent = this.repository.findFlightCrewMemberByEmployeeCode(member.getEmployeeCode());
				uniqueAgent = existingAgent == null || existingAgent.equals(member);

				super.state(context, uniqueAgent, "employeeCode", "acme.validation.agent.duplicated.message");
			}
			{
				boolean matchingCode;

				String employeeCodePrefix = member.getEmployeeCode().substring(0, 2);
				String employeeFirstInitial = member.getUserAccount().getIdentity().getName().substring(0, 1);
				String employeeSecondInitial = member.getUserAccount().getIdentity().getSurname().substring(0, 1);

				matchingCode = Objects.equals(employeeCodePrefix, employeeFirstInitial + employeeSecondInitial);

				super.state(context, matchingCode, "employeeCode", "acme.validation.agent.employeecode.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
