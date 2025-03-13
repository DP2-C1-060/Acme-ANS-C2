
package acme.entities.legs;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("select l from Leg l where substring(l.flightNumber, 4, 4) = ?1")
	Leg findLegByLastFourDigits(String digits);

	@Query("select l from Leg l where l.flightNumber = ?1")
	Leg findLegByFlightNumber(String flightNumber);
}
