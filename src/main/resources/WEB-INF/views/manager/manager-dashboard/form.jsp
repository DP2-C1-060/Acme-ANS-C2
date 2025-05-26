<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title><acme:print code="manager.manager-dashboard.form.title"/></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body class="container py-4">

    <h2 class="mb-3">
        <acme:print code="manager.manager-dasboard.form.title.manager-data"/>
    </h2>
    <table class="table table-bordered w-50">
        <tbody>
        <tr>
            <th scope="row"><acme:print code="manager.manager-dashboard.form.label.ranking"/></th>
            <td><acme:print value="${ranking}"/></td>
        </tr>
        <tr>
            <th scope="row"><acme:print code="manager.manager-dashboard.form.label.retire"/></th>
            <td><acme:print value="${retire}"/></td>
        </tr>
        <tr>
            <th scope="row"><acme:print code="manager.manager-dashboard.form.label.ratio"/></th>
            <td>
                <jstl:if test="${ratio >= 0}">
                    <acme:print value="${ratio}"/>
                </jstl:if>
            </td>
        </tr>
        </tbody>
    </table>

    <h2 class="mt-5 mb-3">
        <acme:print code="manager.manager-dasboard.form.title.total-legs"/>
    </h2>

    <jstl:if test="${totalOfOnTimeLegs == 0 and totalOfDelayedLegs == 0 and totalOfCancelledLegs == 0 and totalOfLandedLegs == 0}">
        <div class="alert alert-warning" role="alert">
            <strong>ℹ️</strong> No hay tramos disponibles para este manager.
        </div>
    </jstl:if>

    <jstl:if test="${totalOfOnTimeLegs != 0 or totalOfDelayedLegs != 0 or totalOfCancelledLegs != 0 or totalOfLandedLegs != 0}">
        <div class="row g-4 align-items-center">
            <div class="col-md-6">
                <canvas id="legsChart"></canvas>
            </div>
            <div class="col-md-6">
                <table class="table table-sm table-striped">
                    <tbody>
                    <tr>
                        <th scope="row"><acme:print code="manager.manager-dashboard.form.label.total-time"/></th>
                        <td><acme:print value="${totalOfOnTimeLegs}"/></td>
                    </tr>
                    <tr>
                        <th scope="row"><acme:print code="manager.manager-dashboard.form.label.total-delayed"/></th>
                        <td><acme:print value="${totalOfDelayedLegs}"/></td>
                    </tr>
                    <tr>
                        <th scope="row"><acme:print code="manager.manager-dashboard.form.label.total-canceled"/></th>
                        <td><acme:print value="${totalOfCancelledLegs}"/></td>
                    </tr>
                    <tr>
                        <th scope="row"><acme:print code="manager.manager-dashboard.form.label.total-landed"/></th>
                        <td><acme:print value="${totalOfLandedLegs}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </jstl:if>

    <h2 class="mt-5 mb-3">
        <acme:print code="manager.manager-dasboard.form.title.flight-EUR-cost"/>
    </h2>

    <jstl:choose>
        <jstl:when test="${averageFlightCostEUR >= 0}">
            <div class="row g-4 align-items-center">
                <div class="col-md-6">
                    <canvas id="flightCostChart"></canvas>
                </div>
                <div class="col-md-6">
                    <table class="table table-sm table-striped">
                        <tbody>
                        <tr>
                            <th scope="row"><acme:print code="manager.manager-dashboard.form.label.flight-average"/></th>
                            <td><acme:print value="${averageFlightCostEUR}"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><acme:print code="manager.manager-dashboard.form.label.flight-deviation"/></th>
                            <td><acme:print value="${deviationOfFlightCostEUR}"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><acme:print code="manager.manager-dashboard.form.label.flight-minimum"/></th>
                            <td><acme:print value="${minimumFlightCostEUR}"/></td>
                        </tr>
                        <tr>
                            <th scope="row"><acme:print code="manager.manager-dashboard.form.label.flight-maximum"/></th>
                            <td><acme:print value="${maximumFlightCostEUR}"/></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </jstl:when>
        <jstl:otherwise>
            <div class="alert alert-warning" role="alert">
                <strong>ℹ️</strong> No hay vuelos publicados con costes en EUR.
            </div>
        </jstl:otherwise>
    </jstl:choose>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            // Renderizar gráfico de tramos si hay datos
            if (${totalOfOnTimeLegs} + ${totalOfDelayedLegs} + ${totalOfCancelledLegs} + ${totalOfLandedLegs} > 0) {
                const legsCtx = document.getElementById('legsChart').getContext('2d');
                new Chart(legsCtx, {
                    type: 'pie',
                    data: {
                        labels: [
                            '<acme:print code="manager.manager-dashboard.form.label.total-time"/>',
                            '<acme:print code="manager.manager-dashboard.form.label.total-delayed"/>',
                            '<acme:print code="manager.manager-dashboard.form.label.total-canceled"/>',
                            '<acme:print code="manager.manager-dashboard.form.label.total-landed"/>'
                        ],
                        datasets: [{
                            data: [
                                ${totalOfOnTimeLegs},
                                ${totalOfDelayedLegs},
                                ${totalOfCancelledLegs},
                                ${totalOfLandedLegs}
                            ]
                        }]
                    },
                    options: {
                        plugins: {
                            legend: {position: 'bottom'}
                        }
                    }
                });
            }

            // Renderizar gráfico de costes si hay datos
            if (${averageFlightCostEUR} >= 0) {
                const flightCtx = document.getElementById('flightCostChart').getContext('2d');
                new Chart(flightCtx, {
                    type: 'bar',
                    data: {
                        labels: [
                            '<acme:print code="manager.manager-dashboard.form.label.flight-average"/>',
                            '<acme:print code="manager.manager-dashboard.form.label.flight-deviation"/>',
                            '<acme:print code="manager.manager-dashboard.form.label.flight-minimum"/>',
                            '<acme:print code="manager.manager-dashboard.form.label.flight-maximum"/>'
                        ],
                        datasets: [{
                            data: [
                                ${averageFlightCostEUR},
                                ${deviationOfFlightCostEUR},
                                ${minimumFlightCostEUR},
                                ${maximumFlightCostEUR}
                            ]
                        }]
                    },
                    options: {
                        scales: {
                            y: {beginAtZero: true}
                        },
                        plugins: {
                            legend: {display: false}
                        }
                    }
                });
            }
        });
    </script>

</body>
</html>
