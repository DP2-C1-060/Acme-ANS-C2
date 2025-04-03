/*
 * AgentClaimRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.agent.claim;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.legs.Leg;
import acme.entities.tracking.Tracking;

@Repository
public interface AgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select c from Claim c where c.agent.id = :agentId")
	Collection<Claim> findClaimsByAgentId(int agentId);

	@Query("select t from Tracking t where t.claim.id = :claimId")
	Collection<Tracking> findTrackingsByClaimId(int claimId);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select l from Leg l " + "join l.aircraft a " + "join a.airline al " + "join Agent ag on ag.airline = al " + "where ag.id = :agentId")
	Collection<Leg> findLegsByAgentId(int agentId);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT t FROM Tracking t WHERE t.claim.id = :claimId ORDER BY t.resolutionPercentage DESC")
	List<Tracking> findLastTrackingsByClaimId(Integer claimId);

}
