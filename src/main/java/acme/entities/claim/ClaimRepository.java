
package acme.entities.claim;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.tracking.Tracking;

public interface ClaimRepository extends AbstractRepository {

	@Query("SELECT t FROM Tracking t WHERE t.claim.id = :claimId AND t.lastUpdateMoment < :lastUpdateMoment")
	List<Tracking> findLastIndicatorByClaimId(Integer claimId, Date lastUpdateMoment);

}
