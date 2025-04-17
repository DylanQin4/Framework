<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.google.gson.Gson" %>
<%
    Gson gson = new Gson();
    String classesJson = gson.toJson(request.getAttribute("classes"));
    String passengerTypesJson = gson.toJson(request.getAttribute("passengerTypes"));
%>

<div class="max-w-5xl mx-auto">
  <h1 class="text-2xl font-semibold mb-6">Ajouter un vol</h1>

  <form action="${pageContext.request.contextPath}/flights/add" method="post" id="flightForm" novalidate class="space-y-8">
    <!-- Numéro de vol -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label for="flightNumber" class="block text-sm font-medium text-gray-700 mb-1">Numéro de vol</label>
        <input
          type="text"
          id="flightNumber"
          name="flightNumber"
          required
          class="block w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
        />
        <p class="mt-1 text-sm text-red-600 hidden" data-error-for="flightNumber">Veuillez saisir un numéro de vol.</p>
      </div>
    </div>

    <!-- Avion -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label for="airplaneId" class="block text-sm font-medium text-gray-700 mb-1">Avion</label>
        <select
          id="airplaneId"
          name="airplaneId"
          required
          class="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
        >
          <option value="">Sélectionnez un avion</option>
          <c:forEach items="${airplanes}" var="airplane">
            <option value="${airplane.id}">${airplane.model} (${airplane.totalSeats} sièges)</option>
          </c:forEach>
        </select>
        <p class="mt-1 text-sm text-red-600 hidden" data-error-for="airplaneId">Veuillez sélectionner un avion.</p>
      </div>
    </div>

    <!-- Villes -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label for="departureCityId" class="block text-sm font-medium text-gray-700 mb-1">Ville de départ</label>
        <select
          id="departureCityId"
          name="departureCityId"
          required
          class="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
        >
          <option value="">Sélectionnez une ville</option>
          <c:forEach items="${cities}" var="city" varStatus="loop">
            <option value="${city.id}" ${loop.index == 0 ? 'selected' : ''}>${city.name} (${city.countryName})</option>
          </c:forEach>
        </select>
        <p class="mt-1 text-sm text-red-600 hidden" data-error-for="departureCityId">Veuillez sélectionner une ville de départ.</p>
      </div>

      <div>
        <label for="arrivalCityId" class="block text-sm font-medium text-gray-700 mb-1">Ville d'arrivée</label>
        <select
          id="arrivalCityId"
          name="arrivalCityId"
          required
          class="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
        >
          <option value="">Sélectionnez une ville</option>
          <c:forEach items="${cities}" var="city" varStatus="loop">
            <option value="${city.id}" ${loop.index == 1 ? 'selected' : ''}>${city.name} (${city.countryName})</option>
          </c:forEach>
        </select>
        <p class="mt-1 text-sm text-red-600 hidden" data-error-for="arrivalCityId">Veuillez sélectionner une ville d'arrivée.</p>
      </div>
    </div>

    <!-- Dates -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label for="departureTime" class="block text-sm font-medium text-gray-700 mb-1">Date et heure de départ</label>
        <input
          type="datetime-local"
          id="departureTime"
          name="departureTime"
          value="${defaultDepartureTime}"
          required
          class="block w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
        />
        <p class="mt-1 text-sm text-red-600 hidden" data-error-for="departureTime">Veuillez saisir une date et heure de départ.</p>
      </div>

      <div>
        <label for="arrivalTime" class="block text-sm font-medium text-gray-700 mb-1">Date et heure d'arrivée</label>
        <input
          type="datetime-local"
          id="arrivalTime"
          name="arrivalTime"
          value="${defaultArrivalTime}"
          required
          class="block w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
        />
        <p class="mt-1 text-sm text-red-600 hidden" data-error-for="arrivalTime">Veuillez saisir une date et heure d'arrivée.</p>
      </div>
    </div>

    <!-- Cutoffs -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label for="reservationCutoffHours" class="block text-sm font-medium text-gray-700 mb-1">Délai de réservation (heures)</label>
        <input
          type="number"
          id="reservationCutoffHours"
          name="reservationCutoffHours"
          min="0"
          value="${not empty reservation_cutoff_hours ? reservation_cutoff_hours : 24}"
          required
          class="block w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
        />
        <p class="mt-1 text-sm text-red-600 hidden" data-error-for="reservationCutoffHours">Veuillez saisir un délai valide.</p>
      </div>

      <div>
        <label for="cancellationCutoffHours" class="block text-sm font-medium text-gray-700 mb-1">Délai d'annulation (heures)</label>
        <input
          type="number"
          id="cancellationCutoffHours"
          name="cancellationCutoffHours"
          min="0"
          value="${not empty cancellation_cutoff_hours ? cancellation_cutoff_hours : 48}"
          required
          class="block w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
        />
        <p class="mt-1 text-sm text-red-600 hidden" data-error-for="cancellationCutoffHours">Veuillez saisir un délai valide.</p>
      </div>
    </div>

    <!-- Tarifs & Promotions par classe/type -->
    <h2 class="text-lg font-semibold">Tarifs et Promotions par Classe et Type de Passager</h2>
    <div class="space-y-6">
      <c:forEach items="${classes}" var="clazz">
        <section class="rounded-xl border border-gray-200 bg-white shadow-sm">
          <header class="px-4 py-3 border-b border-gray-200 font-medium">
            Classe : ${clazz.label}
          </header>
          <div class="p-4 space-y-6">
            <c:forEach items="${passengerTypes}" var="passengerType">
              <div class="space-y-3">
                <div class="text-sm font-semibold text-gray-700">
                  Type de passager : ${passengerType.typeName}
                </div>

                <!-- Champs cachés -->
                <input type="hidden" name="classId_${clazz.id}_${passengerType.id}" value="${clazz.id}">
                <input type="hidden" name="passengerTypeId_${clazz.id}_${passengerType.id}" value="${passengerType.id}">

                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div>
                    <label for="basePrice_${clazz.id}_${passengerType.id}" class="block text-sm font-medium text-gray-700 mb-1">Prix de base</label>
                    <input
                      type="number"
                      step="0.01"
                      id="basePrice_${clazz.id}_${passengerType.id}"
                      name="basePrice_${clazz.id}_${passengerType.id}"
                      value="${not empty defaultPrices[passengerType.id] ? defaultPrices[passengerType.id] : 0.0}"
                      required
                      class="block w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
                    />
                  </div>
                  <div>
                    <label for="promotionLimit_${clazz.id}_${passengerType.id}" class="block text-sm font-medium text-gray-700 mb-1">Limite de promotion</label>
                    <input
                      type="number"
                      id="promotionLimit_${clazz.id}_${passengerType.id}"
                      name="promotionLimit_${clazz.id}_${passengerType.id}"
                      value="${not empty promotion_limit ? promotion_limit : 0}"
                      required
                      class="block w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
                    />
                  </div>
                  <div>
                    <label for="promotionDiscount_${clazz.id}_${passengerType.id}" class="block text-sm font-medium text-gray-700 mb-1">Réduction (%)</label>
                    <input
                      type="number"
                      step="0.01"
                      id="promotionDiscount_${clazz.id}_${passengerType.id}"
                      name="promotionDiscount_${clazz.id}_${passengerType.id}"
                      value="${not empty promotion_discount ? promotion_discount : 0}"
                      required
                      class="block w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
                    />
                  </div>
                </div>
              </div>
            </c:forEach>
          </div>
        </section>
      </c:forEach>
    </div>

    <!-- Champ caché pour la chaîne agrégée -->
    <input type="hidden" id="flightClassPassengerData" name="flightClassPassengerData"/>

    <!-- Actions -->
    <div class="flex items-center gap-3">
      <button
        type="submit"
        class="inline-flex items-center px-5 py-2.5 rounded-lg bg-blue-600 text-white hover:bg-blue-700 transition"
      >
        Enregistrer
      </button>
      <a
        href="${pageContext.request.contextPath}/flights"
        class="inline-flex items-center px-5 py-2.5 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-50 transition"
      >
        Annuler
      </a>
    </div>
  </form>
</div>

<script>
  document.addEventListener("DOMContentLoaded", function() {
    try {
      const classes = <%= classesJson %>;
      const passengerTypes = <%= passengerTypesJson %>;
      console.log("Classes data:", classes);
      console.log("Passenger Types data:", passengerTypes);

      document.getElementById("flightForm").addEventListener("submit", function (event) {
        // Validation légère (HTML5) + styles d’erreur Tailwind
        const form = event.target;
        const invalidFields = Array.from(form.querySelectorAll("[required]")).filter(el => !el.checkValidity());
        form.querySelectorAll(".ring-2").forEach(el => el.classList.remove("ring-2","ring-red-600","border-red-600"));
        form.querySelectorAll("[data-error-for]").forEach(p => p.classList.add("hidden"));

        if (invalidFields.length > 0) {
          event.preventDefault();
          event.stopPropagation();
          invalidFields.forEach(el => {
            el.classList.add("ring-2","ring-red-600","border-red-600");
            const err = form.querySelector(`[data-error-for="${el.id}"]`);
            if (err) err.classList.remove("hidden");
          });
          return;
        }

        let flightClassPassengerData = "";

        if (!classes || !passengerTypes) {
          console.error("Classes or PassengerTypes data is missing!");
          return;
        }

        classes.forEach(clazz => {
          passengerTypes.forEach(passengerType => {
            const classId = clazz.id;
            const passengerTypeId = passengerType.id;

            if (classId === undefined || passengerTypeId === undefined) {
              console.error("Could not find ID properties for:", clazz, passengerType);
              return;
            }

            const basePriceInput = document.querySelector(`input[name="basePrice_${classId}_${passengerTypeId}"]`);
            const promotionLimitInput = document.querySelector(`input[name="promotionLimit_${classId}_${passengerTypeId}"]`);
            const promotionDiscountInput = document.querySelector(`input[name="promotionDiscount_${classId}_${passengerTypeId}"]`);

            if (basePriceInput && promotionLimitInput && promotionDiscountInput) {
              const basePrice = parseFloat(basePriceInput.value);
              const promotionLimit = parseInt(promotionLimitInput.value);
              const promotionDiscount = parseFloat(promotionDiscountInput.value);

              if (isNaN(basePrice) || isNaN(promotionLimit) || isNaN(promotionDiscount)) {
                console.warn(`Invalid input values for class ${classId}, type ${passengerTypeId}. Skipping.`);
                return;
              }

              flightClassPassengerData += `${classId},${passengerTypeId},${basePrice},${promotionLimit},${promotionDiscount}|`;
            } else {
              console.error(`Input element missing for class ${classId}, type ${passengerTypeId}.`, {
                basePriceInput, promotionLimitInput, promotionDiscountInput
              });
            }
          });
        });

        if (flightClassPassengerData.endsWith("|")) {
          flightClassPassengerData = flightClassPassengerData.slice(0, -1);
        }

        document.getElementById("flightClassPassengerData").value = flightClassPassengerData;
        console.log("Generated flightClassPassengerData:", flightClassPassengerData);
      });
    } catch (e) {
      console.error("Error initializing form script:", e);
    }
  });
</script>