
package acme.entities.legs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("select l from Leg l where substring(l.flightNumber, 4, 4) = ?1")
	Leg findLegByLastFourDigits(String digits);

	@Query("select l from Leg l where l.flightNumber = ?1")
	Leg findLegByFlightNumber(String flightNumber);

	@Query("select l from Leg l where l.flight.id = ?1 and l.draftMode = false")
	List<Leg> findPublishedLegsByFlightId(int id);

	@Query("select l from Leg l where l.aircraft.registrationNumber = ?1 and l.draftMode = false")
	List<Leg> findPublishedLegsByAircraft(String registrationNumber);

	@Query("select l from Leg l WHERE SUBSTRING(l.flightNumber, LENGTH(l.flightNumber) - 3, 4) = ?1")
	List<Leg> findLegsByLast4Digits(String last4);
}
