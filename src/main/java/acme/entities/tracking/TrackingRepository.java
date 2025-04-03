
package acme.entities.tracking;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface TrackingRepository extends AbstractRepository {

	@Query("SELECT t FROM Tracking t WHERE t.claim.id = :claimId AND t.lastUpdateMoment < :lastUpdateMoment")
	List<Tracking> findByClaimIdAndDateBefore(Integer claimId, Date lastUpdateMoment);

	@Query("SELECT t FROM Tracking t WHERE t.claim.id = :claimId ORDER BY t.resolutionPercentage DESC")
	List<Tracking> findLastTrackingsByClaimId(Integer claimId);
}
