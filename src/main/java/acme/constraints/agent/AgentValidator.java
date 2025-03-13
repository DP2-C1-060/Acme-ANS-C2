
package acme.constraints.agent;

import java.util.Date;
import java.util.Objects;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.realms.agent.Agent;
import acme.realms.agent.AgentRepository;

public class AgentValidator extends AbstractValidator<ValidAgent, Agent> {

	@Autowired
	private AgentRepository repository;


	@Override
	protected void initialise(final ValidAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Agent agent, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (agent == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueAgent;
				Agent existingAgent;

				existingAgent = this.repository.findAgentByEmployeeCode(agent.getEmployeeCode());
				uniqueAgent = existingAgent == null || existingAgent.equals(agent);

				super.state(context, uniqueAgent, "employeeCode", "acme.validation.job.duplicated-employeeCode.message");
			}
			{
				boolean matchingCode;

				String employeeCodePrefix = agent.getEmployeeCode().substring(0, 2);
				String employeeFirstInitial = agent.getUserAccount().getIdentity().getName().substring(0, 1);
				String employeeSecondInitial = agent.getUserAccount().getIdentity().getSurname().substring(0, 1);

				matchingCode = Objects.equals(employeeCodePrefix, employeeFirstInitial + employeeSecondInitial);

				super.state(context, matchingCode, "employeeCode", "acme.validation.agent.employeecode.message");
			}
			{
				boolean consistentMoment;

				Date workStartMoment = agent.getWorkStartMoment();
				Date airlineFoundationDate = agent.getAirline().getFoundationDate();

				consistentMoment = workStartMoment.after(airlineFoundationDate);

				super.state(context, consistentMoment, "workStartMoment", "acme.validation.agent.workStartMoment.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
