
package acme.entities.booking;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;

public interface PassengerRepository extends AbstractRepository {

	// Retorna una lista de Passenger que tengan el mismo passportNumber
	@Query("SELECT p FROM Passenger p WHERE p.passportNumber = :passportNumber")
	List<Passenger> findAllByPassportNumber(@Param("passportNumber") String passportNumber);

}
