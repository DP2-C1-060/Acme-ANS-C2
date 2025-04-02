
package acme.features.manager.leg;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select l.flight from Leg l where l.id = :id")
	Flight findFlightByLegId(int id);

	@Query("select l from Leg l where l.flight.id = :flightId")
	List<Leg> findLegsByFlightId(int flightId);

	@Query("select a from Aircraft a where a.status = 'ACTIVE'")
	List<Aircraft> findAircrafts();

	@Query("select ap from Airport ap")
	List<Airport> findAirports();

	@Query("select a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(int id);

	@Query("select ap from Airport ap where ap.id = :id")
	Airport findAirportById(int id);

	@Query("select l from Leg l where l.flight.id = ?1 and l.draftMode = false order  by l.scheduledDeparture ASC")
	List<Leg> findPublishedLegsByFlightId(int id);

}
