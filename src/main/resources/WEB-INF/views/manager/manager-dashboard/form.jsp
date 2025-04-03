<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="manager.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.ranking-position"/>
		</th>
		<td>
			<acme:print value="${rankingPosition}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.years-to-retirement"/>
		</th>
		<td>
			<acme:print value="${yearsToRetirement}"/>
		</td>
	</tr>
</table>

<h2>
	<acme:print code="manager.dashboard.form.title.legs-status"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.legs-on-time"/>
		</th>
		<td>
			<acme:print value="${legsOnTime}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.legs-delayed"/>
		</th>
		<td>
			<acme:print value="${legsDelayed}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.legs-cancelled"/>
		</th>
		<td>
			<acme:print value="${legsCancelled}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.legs-landed"/>
		</th>
		<td>
			<acme:print value="${legsLanded}"/>
		</td>
	</tr>
</table>

<h2>
	<acme:print code="manager.dashboard.form.title.legs-ratios"/>
</h2>

<div>
	<canvas id="canvasLegs"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [ "ON TIME", "DELAYED" ],
			datasets : [
				{
					data : [
						<jstl:out value="${ratioOnTimeLegs}"/>, 
						<jstl:out value="${ratioDelayedLegs}"/>
					]
				}
			]
		};
		var options = {
			scales : {
				yAxes : [
					{
						ticks : {
							suggestedMin : 0.0,
							suggestedMax : 1.0
						}
					}
				]
			},
			legend : {
				display : false
			}
		};

		var canvas = document.getElementById("canvasLegs");
		var context = canvas.getContext("2d");
		new Chart(context, {
			type : "bar",
			data : data,
			options : options
		});
	});
</script>

<h2>
	<acme:print code="manager.dashboard.form.title.flight-costs"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.average-flight-cost"/>
		</th>
		<td>
			<acme:print value="${averageFlightCost}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.min-flight-cost"/>
		</th>
		<td>
			<acme:print value="${minFlightCost}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.max-flight-cost"/>
		</th>
		<td>
			<acme:print value="${maxFlightCost}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.stddev-flight-cost"/>
		</th>
		<td>
			<acme:print value="${stdDevFlightCost}"/>
		</td>
	</tr>
</table>

<h2>
	<acme:print code="manager.dashboard.form.title.airports"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.most-popular-airport"/>
		</th>
		<td>
			<acme:print value="${mostPopularAirport}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.least-popular-airport"/>
		</th>
		<td>
			<acme:print value="${leastPopularAirport}"/>
		</td>
	</tr>
</table>

<acme:return/>
