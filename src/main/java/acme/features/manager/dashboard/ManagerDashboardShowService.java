
package acme.features.manager.dashboard;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.legs.LegStatus;
import acme.forms.ManagerDashboard;
import acme.realms.manager.Manager;

@GuiService
public class ManagerDashboardShowService extends AbstractGuiService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		ManagerDashboard dashboard;
		int id = super.getRequest().getPrincipal().getAccountId();
		Manager manager = this.repository.findManagerByAccountId(id);

		Integer ranking;
		Integer retire;
		double ratio;
		String mostPopularAirport;
		String lessPopularAirport;

		Integer totalOfOnTimeLegs;
		Integer totalOfDelayed;
		Integer totalOfCanceled;
		Integer totalOfLanded;

		double averageFlightCostEUR = -1;
		double deviationOfFlightCostEUR = -1;
		double minimumFlightCostEUR = -1;
		double maximumFlightCostEUR = -1;

		List<Manager> managers = this.repository.getAllManagers();
		managers.sort(Comparator.comparing(Manager::getYearsOfExperience));
		ranking = managers.indexOf(manager) + 1;
		retire = 65 - manager.getYearsOfExperience();

		try {
			int onTime = this.repository.getManagerLegsByStatus(manager.getId(), LegStatus.ON_TIME).size();
			int delayed = this.repository.getManagerLegsByStatus(manager.getId(), LegStatus.DELAYED).size();
			ratio = delayed != 0 ? (double) onTime / delayed : -1;
		} catch (Exception e) {
			ratio = -1;
		}

		totalOfOnTimeLegs = this.repository.getManagerLegsByStatus(manager.getId(), LegStatus.ON_TIME).size();
		totalOfDelayed = this.repository.getManagerLegsByStatus(manager.getId(), LegStatus.DELAYED).size();
		totalOfCanceled = this.repository.getManagerLegsByStatus(manager.getId(), LegStatus.CANCELLED).size();
		totalOfLanded = this.repository.getManagerLegsByStatus(manager.getId(), LegStatus.LANDED).size();

		try {
			List<Object[]> rawCosts = this.repository.findAllFlightCosts(manager.getId());

			List<Double> eurCosts = rawCosts.stream().map(obj -> {
				double amount = (Double) obj[0];
				String currency = (String) obj[1];
				return switch (currency) {
				case "USD" -> amount * 0.92;
				case "GBP" -> amount * 1.15;
				case "EUR" -> amount;
				default -> amount;
				};
			}).toList();

			if (!eurCosts.isEmpty()) {
				double avg = eurCosts.stream().mapToDouble(Double::doubleValue).average().orElse(-1);
				double dev = Math.sqrt(eurCosts.stream().mapToDouble(v -> Math.pow(v - avg, 2)).average().orElse(0));
				double min = eurCosts.stream().mapToDouble(Double::doubleValue).min().orElse(-1);
				double max = eurCosts.stream().mapToDouble(Double::doubleValue).max().orElse(-1);

				averageFlightCostEUR = avg;
				deviationOfFlightCostEUR = dev;
				minimumFlightCostEUR = min;
				maximumFlightCostEUR = max;
			}
		} catch (Exception e) {
			// valores ya inicializados a -1
		}

		dashboard = new ManagerDashboard();
		dashboard.setRanking(ranking);
		dashboard.setRetire(retire);
		dashboard.setRatio(ratio);
		dashboard.setTotalOfOnTimeLegs(totalOfOnTimeLegs);
		dashboard.setTotalOfDelayedLegs(totalOfDelayed);
		dashboard.setTotalOfCancelledLegs(totalOfCanceled);
		dashboard.setTotalOfLandedLegs(totalOfLanded);
		dashboard.setAverageFlightCostEUR(averageFlightCostEUR);
		dashboard.setDeviationOfFlightCostEUR(deviationOfFlightCostEUR);
		dashboard.setMinimumFlightCostEUR(minimumFlightCostEUR);
		dashboard.setMaximumFlightCostEUR(maximumFlightCostEUR);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, //
			"ranking", "retire", "ratio", "totalOfCancelledLegs", "totalOfDelayedLegs", "totalOfLandedLegs", "totalOfOnTimeLegs", //
			"averageFlightCostEUR", "deviationOfFlightCostEUR", "minimumFlightCostEUR", "maximumFlightCostEUR");

		super.getResponse().addData(dataset);
	}
}
