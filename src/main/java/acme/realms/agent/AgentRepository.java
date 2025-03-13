
package acme.realms.agent;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AgentRepository extends AbstractRepository {

	@Query("select a from Agent a where a.employeeCode = :employeeCode")
	Agent findAgentByEmployeeCode(String employeeCode);

}
