
package acme.entities.trackinglogs;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface TrackingLogsRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLogs t WHERE t.claim.id = :claimId AND t.lastUpdateMoment < :lastUpdateMoment")
	List<TrackingLogs> findByClaimIdAndDateBefore(Integer claimId, Date lastUpdateMoment);

}
