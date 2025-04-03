
package acme.features.manager.managerDashboard;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.legs.Leg;
import acme.forms.manager.ManagerDashboard;
import acme.realms.manager.Manager;

@GuiService
public class ManagerManagerDashboardShowService extends AbstractGuiService<Manager, ManagerDashboard> {

	@Autowired
	private ManagerManagerDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Integer managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		ManagerDashboard dashboard;
		// Ranking: se suma 1 al número de managers con mayor experiencia
		Integer countMoreExp = this.repository.countManagersWithMoreExperience(managerId);
		Integer rankingPosition = countMoreExp + 1;
		Integer yearsToRetire = this.repository.yearsToRetire(managerId);
		Double ratioOnTimeLegs = this.repository.ratioOnTimeLegs(managerId);
		Double ratioDelayedLegs = this.repository.ratioDelayedLegs(managerId);

		// Se obtienen todos los legs y se cuentan los aeropuertos (departure y arrival)
		List<Leg> legs = this.repository.findLegsByManager(managerId);
		Map<String, Integer> airportCount = new HashMap<>();
		for (Leg leg : legs) {
			// Contar aeropuerto de salida
			String departureName = leg.getDepartureAirport().getName();
			airportCount.put(departureName, airportCount.getOrDefault(departureName, 0) + 1);
			// Contar aeropuerto de llegada
			String arrivalName = leg.getArrivalAirport().getName();
			airportCount.put(arrivalName, airportCount.getOrDefault(arrivalName, 0) + 1);
		}

		String mostPopularAirport = "N/A";
		String leastPopularAirport = "N/A";
		if (!airportCount.isEmpty()) {
			// Se obtiene el máximo y el mínimo de apariciones
			int maxCount = Collections.max(airportCount.values());
			int minCount = Collections.min(airportCount.values());
			// Se selecciona el primer aeropuerto que cumpla con el máximo y el mínimo
			for (Entry<String, Integer> entry : airportCount.entrySet())
				if (entry.getValue() == maxCount) {
					mostPopularAirport = entry.getKey();
					break;
				}
			for (Entry<String, Integer> entry : airportCount.entrySet())
				if (entry.getValue() == minCount) {
					leastPopularAirport = entry.getKey();
					break;
				}
		}

		Integer legsOnTime = this.repository.legsOnTime(managerId);
		Integer legsDelayed = this.repository.legsDelayed(managerId);
		Integer legsCancelled = this.repository.legsCancelled(managerId);
		Integer legsLanded = this.repository.legsLanded(managerId);
		Double averageFlightCost = this.repository.averageFlightCost(managerId);
		Double minFlightCost = this.repository.minFlightCost(managerId);
		Double maxFlightCost = this.repository.maxFlightCost(managerId);
		Double stdDevFlightCost = this.repository.stdDevFlightCost(managerId);

		dashboard = new ManagerDashboard();
		dashboard.setRankingPosition(rankingPosition);
		dashboard.setYearsToRetirement(yearsToRetire);
		dashboard.setRatioOnTimeLegs(ratioOnTimeLegs);
		dashboard.setRatioDelayedLegs(ratioDelayedLegs);
		dashboard.setMostPopularAirport(mostPopularAirport);
		dashboard.setLeastPopularAirport(leastPopularAirport);
		dashboard.setLegsOnTime(legsOnTime);
		dashboard.setLegsDelayed(legsDelayed);
		dashboard.setLegsCancelled(legsCancelled);
		dashboard.setLegsLanded(legsLanded);
		dashboard.setAverageFlightCost(averageFlightCost);
		dashboard.setMinFlightCost(minFlightCost);
		dashboard.setMaxFlightCost(maxFlightCost);
		dashboard.setStdDevFlightCost(stdDevFlightCost);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "rankingPosition", "yearsToRetirement", "ratioOnTimeLegs", "ratioDelayedLegs", "mostPopularAirport", "leastPopularAirport", "legsOnTime", "legsDelayed", "legsCancelled", "legsLanded",
			"averageFlightCost", "minFlightCost", "maxFlightCost", "stdDevFlightCost");
		super.getResponse().addData(dataset);
	}
}
