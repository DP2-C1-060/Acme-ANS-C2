
package acme.entities.bannedPassengers;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;

public interface BannedPassengerRepository extends AbstractRepository {

	@Query("SELECT bp FROM BannedPassenger bp WHERE bp.passportNumber = :passportNumber")
	BannedPassenger findOneByPassportNumber(@Param("passportNumber") String passportNumber);

}
