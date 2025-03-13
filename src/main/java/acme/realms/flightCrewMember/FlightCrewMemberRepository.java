
package acme.realms.flightCrewMember;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("select a from FlightCrewMember a where a.employeeCode = :employeeCode")
	FlightCrewMember findFlightCrewMemberByEmployeeCode(String employeeCode);

}
