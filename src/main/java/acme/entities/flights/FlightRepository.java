
package acme.entities.flights;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.legs.Leg;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture asc")
	List<Leg> findLegsByFlightId(int flightId);

	@Query("select l from Leg l where l.flight.id = ?1 and l.draftMode = true")
	List<Leg> findDraftLegsByFlightId(int i);

}
