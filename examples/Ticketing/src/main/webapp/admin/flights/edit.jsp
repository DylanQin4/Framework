<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:formatDate value="${flight.departureTimeAsDate}" pattern="yyyy-MM-dd'T'HH:mm" var="depLocal"/>
<fmt:formatDate value="${flight.arrivalTimeAsDate}"   pattern="yyyy-MM-dd'T'HH:mm" var="arrLocal"/>

<div class="max-w-5xl mx-auto p-4">
  <h1 class="text-2xl font-semibold mb-6">Modifier un Vol</h1>

  <form action="${pageContext.request.contextPath}/flights/edit"
        method="post"
        id="flightForm"
        class="needs-validation space-y-8"
        novalidate>

    <input type="hidden" name="id" value="${flight.id}"/>

    <!-- Numéro de vol -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label for="flightNumber" class="block text-sm font-medium text-gray-700 mb-1">Numéro de vol</label>
        <input type="text"
               id="flightNumber"
               name="flightNumber"
               value="${flight.flightNumber}"
               required
               class="block w-full rounded-lg border border-gray-300 px-3 py-2
                      focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600">
        <div class="mt-1 text-sm text-red-600 hidden">Veuillez saisir un numéro de vol.</div>
      </div>
    </div>

    <!-- Avion -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label for="airplaneId" class="block text-sm font-medium text-gray-700 mb-1">Avion</label>
        <select id="airplaneId"
                name="airplaneId"
                required
                class="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2
                       focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600">
          <option value="">Sélectionnez un avion</option>
          <c:forEach items="${airplanes}" var="airplane">
            <option value="${airplane.id}" ${flight.airplaneId eq airplane.id ? 'selected' : ''}>
              ${airplane.model} (${airplane.totalSeats} sièges)
            </option>
          </c:forEach>
        </select>
        <div class="mt-1 text-sm text-red-600 hidden">Veuillez sélectionner un avion.</div>
      </div>
    </div>

    <!-- Villes -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label for="departureCityId" class="block text-sm font-medium text-gray-700 mb-1">Ville de départ</label>
        <select id="departureCityId"
                name="departureCityId"
                required
                class="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2
                       focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600">
          <option value="">Sélectionnez une ville</option>
          <c:forEach items="${cities}" var="city">
            <option value="${city.id}" ${flight.departureCityId eq city.id ? 'selected' : ''}>
              ${city.name} (${city.countryName})
            </option>
          </c:forEach>
        </select>
        <div class="mt-1 text-sm text-red-600 hidden">Veuillez sélectionner une ville de départ.</div>
      </div>

      <div>
        <label for="arrivalCityId" class="block text-sm font-medium text-gray-700 mb-1">Ville d'arrivée</label>
        <select id="arrivalCityId"
                name="arrivalCityId"
                required
                class="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2
                       focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600">
          <option value="">Sélectionnez une ville</option>
          <c:forEach items="${cities}" var="city">
            <option value="${city.id}" ${flight.arrivalCityId eq city.id ? 'selected' : ''}>
              ${city.name} (${city.countryName})
            </option>
          </c:forEach>
        </select>
        <div class="mt-1 text-sm text-red-600 hidden">Veuillez sélectionner une ville d'arrivée.</div>
      </div>
    </div>

    <!-- Dates -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label for="departureTime" class="block text-sm font-medium text-gray-700 mb-1">Date et heure de départ</label>
        <input type="datetime-local"
               id="departureTime"
               name="departureTime"
               value="${depLocal}"
               required
               class="block w-full rounded-lg border border-gray-300 px-3 py-2
                      focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600">
        <div class="mt-1 text-sm text-red-600 hidden">Veuillez saisir une date et heure de départ.</div>
      </div>

      <div>
        <label for="arrivalTime" class="block text-sm font-medium text-gray-700 mb-1">Date et heure d'arrivée</label>
        <input type="datetime-local"
               id="arrivalTime"
               name="arrivalTime"
               value="${arrLocal}"
               required
               class="block w-full rounded-lg border border-gray-300 px-3 py-2
                      focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600">
        <div class="mt-1 text-sm text-red-600 hidden">Veuillez saisir une date et heure d'arrivée.</div>
      </div>
    </div>

    <!-- Délais -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label for="reservationCutoffHours" class="block text-sm font-medium text-gray-700 mb-1">Délai de réservation (heures)</label>
        <input type="number"
               id="reservationCutoffHours"
               name="reservationCutoffHours"
               value="${flight.reservationCutoffHours}"
               min="0"
               required
               class="block w-full rounded-lg border border-gray-300 px-3 py-2
                      focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600">
        <div class="mt-1 text-sm text-red-600 hidden">Veuillez saisir un délai valide.</div>
      </div>

      <div>
        <label for="cancellationCutoffHours" class="block text-sm font-medium text-gray-700 mb-1">Délai d'annulation (heures)</label>
        <input type="number"
               id="cancellationCutoffHours"
               name="cancellationCutoffHours"
               value="${flight.cancellationCutoffHours}"
               min="0"
               required
               class="block w-full rounded-lg border border-gray-300 px-3 py-2
                      focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600">
        <div class="mt-1 text-sm text-red-600 hidden">Veuillez saisir un délai valide.</div>
      </div>
    </div>

    <!-- Tarifs & Promotions par Classe et Type de Passager (READONLY) -->
    <h4 class="text-lg font-semibold">Tarifs et Promotions par Classe et Type de Passager</h4>
    <c:forEach items="${classes}" var="clazz">
      <div class="rounded-xl border border-gray-200 bg-white shadow-sm mb-3">
        <div class="px-4 py-3 border-b border-gray-200 font-medium">Classe : ${clazz.label}</div>
        <div class="p-4 space-y-6" aria-disabled="true">
          <c:forEach items="${passengerTypes}" var="passengerType">
            <div class="space-y-3">
              <div class="text-sm font-semibold text-gray-700">
                Type de passager : ${passengerType.typeName}
              </div>

              <!-- Récupérer les valeurs existantes pour cette classe et ce type -->
              <c:set var="existingFcp" value="" />
              <c:forEach items="${flightClassPassengers}" var="fcp">
                <c:if test="${fcp.classId eq clazz.id and fcp.passengerTypeId eq passengerType.id}">
                  <c:set var="existingFcp" value="${fcp}" />
                </c:if>
              </c:forEach>

              <!-- Champs cachés (conservés) -->
              <input type="hidden" name="classId_${clazz.id}_${passengerType.id}" value="${clazz.id}">
              <input type="hidden" name="passengerTypeId_${clazz.id}_${passengerType.id}" value="${passengerType.id}">

              <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                  <label for="basePrice_${clazz.id}_${passengerType.id}" class="block text-sm font-medium text-gray-700 mb-1">Prix de base</label>
                  <input type="number" step="0.01"
                         id="basePrice_${clazz.id}_${passengerType.id}"
                         name="basePrice_${clazz.id}_${passengerType.id}"
                         value="${not empty existingFcp ? existingFcp.basePrice : 0.0}"
                         readonly
                         class="block w-full rounded-lg border border-gray-200 bg-gray-100 text-gray-600 cursor-not-allowed
                                px-3 py-2 focus:outline-none focus:ring-0 focus:border-gray-200">
                </div>

                <div>
                  <label for="promotionLimit_${clazz.id}_${passengerType.id}" class="block text-sm font-medium text-gray-700 mb-1">Limite de promotion</label>
                  <input type="number"
                         id="promotionLimit_${clazz.id}_${passengerType.id}"
                         name="promotionLimit_${clazz.id}_${passengerType.id}"
                         value="${not empty existingFcp ? existingFcp.promotionLimit : 0}"
                         readonly
                         class="block w-full rounded-lg border border-gray-200 bg-gray-100 text-gray-600 cursor-not-allowed
                                px-3 py-2 focus:outline-none focus:ring-0 focus:border-gray-200">
                </div>

                <div>
                  <label for="promotionDiscount_${clazz.id}_${passengerType.id}" class="block text-sm font-medium text-gray-700 mb-1">Réduction (%)</label>
                  <input type="number" step="0.01"
                         id="promotionDiscount_${clazz.id}_${passengerType.id}"
                         name="promotionDiscount_${clazz.id}_${passengerType.id}"
                         value="${not empty existingFcp ? existingFcp.promotionDiscount : 0}"
                         readonly
                         class="block w-full rounded-lg border border-gray-200 bg-gray-100 text-gray-600 cursor-not-allowed
                                px-3 py-2 focus:outline-none focus:ring-0 focus:border-gray-200">
                </div>
              </div>
            </div>
          </c:forEach>
        </div>
      </div>
    </c:forEach>

    <!-- Actions -->
    <div class="flex items-center gap-3">
      <button type="submit"
              class="inline-flex items-center px-5 py-2.5 rounded-lg bg-blue-600 text-white hover:bg-blue-700 transition">
        Enregistrer les modifications
      </button>
      <a href="${pageContext.request.contextPath}/flights"
         class="inline-flex items-center px-5 py-2.5 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-50 transition">
        Annuler
      </a>
    </div>
  </form>
</div>
