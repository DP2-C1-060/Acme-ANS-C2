
package acme.features.manager.managerDashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.legs.Leg;

@Repository
public interface ManagerManagerDashboardRepository extends AbstractRepository {

	// Ranking: se cuenta el número de managers con mayor experiencia y se le suma 1
	@Query("SELECT COUNT(m) FROM Manager m WHERE m.yearsOfExperience > (SELECT m2.yearsOfExperience FROM Manager m2 WHERE m2.id = :managerId)")
	Integer countManagersWithMoreExperience(Integer managerId);

	// Años para jubilarse (retirada a 65 años)
	@Query("SELECT 65 - (FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', m.dateOfBirth)) FROM Manager m WHERE m.id = :managerId")
	Integer yearsToRetire(Integer managerId);

	// Ratio de tramos a tiempo
	@Query("SELECT 1.0 * COUNT(l) / (SELECT COUNT(l2) FROM Leg l2 WHERE l2.flight.manager.id = :managerId) " + "FROM Leg l WHERE l.flight.manager.id = :managerId AND l.status = acme.entities.legs.LegStatus.ON_TIME")
	Double ratioOnTimeLegs(Integer managerId);

	// Ratio de tramos retrasados
	@Query("SELECT 1.0 * COUNT(l) / (SELECT COUNT(l2) FROM Leg l2 WHERE l2.flight.manager.id = :managerId) " + "FROM Leg l WHERE l.flight.manager.id = :managerId AND l.status = acme.entities.legs.LegStatus.DELAYED")
	Double ratioDelayedLegs(Integer managerId);

	// Número de tramos agrupados por estado
	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight.manager.id = :managerId AND l.status = acme.entities.legs.LegStatus.ON_TIME")
	Integer legsOnTime(Integer managerId);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight.manager.id = :managerId AND l.status = acme.entities.legs.LegStatus.DELAYED")
	Integer legsDelayed(Integer managerId);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight.manager.id = :managerId AND l.status = acme.entities.legs.LegStatus.CANCELLED")
	Integer legsCancelled(Integer managerId);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight.manager.id = :managerId AND l.status = acme.entities.legs.LegStatus.LANDED")
	Integer legsLanded(Integer managerId);

	// Indicadores de coste de los vuelos
	@Query("SELECT COALESCE(AVG(f.cost.amount), 0) FROM Flight f WHERE f.manager.id = :managerId")
	Double averageFlightCost(Integer managerId);

	@Query("SELECT COALESCE(MIN(f.cost.amount), 0) FROM Flight f WHERE f.manager.id = :managerId")
	Double minFlightCost(Integer managerId);

	@Query("SELECT COALESCE(MAX(f.cost.amount), 0) FROM Flight f WHERE f.manager.id = :managerId")
	Double maxFlightCost(Integer managerId);

	@Query("SELECT COALESCE(STDDEV(f.cost.amount), 0) FROM Flight f WHERE f.manager.id = :managerId")
	Double stdDevFlightCost(Integer managerId);

	// Función para obtener todos los legs de los vuelos del manager
	@Query("SELECT l FROM Leg l WHERE l.flight.manager.id = :managerId")
	List<Leg> findLegsByManager(Integer managerId);
}
