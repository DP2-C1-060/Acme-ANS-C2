
package acme.features.authenticated.manager;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.realms.manager.Manager;

@Repository
public interface AuthenticatedManagerRepository extends AbstractRepository {

	@Query("select m from Manager m where m.userAccount.id = :id")
	Manager findOneManagerByUserAccountId(int id);

	@Query("select m from Manager m where m.identifier = :identifier")
	Manager findManagerByIdentifier(String identifier);

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select a from Airline a")
	List<Airline> findAirlines();

}
