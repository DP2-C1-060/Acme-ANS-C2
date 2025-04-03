
package acme.features.agent.tracking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.tracking.Tracking;

@Repository
public interface AgentTrackingRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select t.claim from Tracking t where t.id = :id")
	Claim findClaimByTrackingId(int id);

	@Query("select t from Tracking t where t.id = :id")
	Tracking findTrackingById(int id);

	@Query("select t from Tracking t where t.claim.id = :masterId")
	Collection<Tracking> findTrackingsByMasterId(int masterId);

}
