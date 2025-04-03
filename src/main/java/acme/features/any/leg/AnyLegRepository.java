
package acme.features.any.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

@Repository
public interface AnyLegRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = ?1")
	Flight findFlightById(int masterId);

	@Query("select l from Leg l where l.flight.id = ?1")
	Collection<Leg> findLegsByFlightId(int masterId);

	@Query("select l from Leg l where l.id = ?1")
	Leg findLegById(int id);

	@Query("SELECT l.flight FROM Leg l WHERE l.id = ?1")
	Flight findFlightByLegId(int id);
}
